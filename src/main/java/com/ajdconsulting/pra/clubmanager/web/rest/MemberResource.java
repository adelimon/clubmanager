package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.data.export.excel.BasicSingleSheetWorkbook;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelHttpOutputStream;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelWorkbook;
import com.ajdconsulting.pra.clubmanager.dates.CurrentFiscalYear;
import com.ajdconsulting.pra.clubmanager.domain.*;
import com.ajdconsulting.pra.clubmanager.integrations.mailchimp.MailingList;
import com.ajdconsulting.pra.clubmanager.renewals.EmailContent;
import com.ajdconsulting.pra.clubmanager.repository.*;
import com.ajdconsulting.pra.clubmanager.scheduled.PointsNotificationTask;
import com.ajdconsulting.pra.clubmanager.security.AuthoritiesConstants;
import com.ajdconsulting.pra.clubmanager.security.SecurityUtils;
import com.ajdconsulting.pra.clubmanager.service.BillingService;
import com.ajdconsulting.pra.clubmanager.service.MailService;
import com.ajdconsulting.pra.clubmanager.service.UserService;
import com.ajdconsulting.pra.clubmanager.web.rest.util.HeaderUtil;
import com.ajdconsulting.pra.clubmanager.web.rest.util.PaginationUtil;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelSqlReport;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;;

/**
 * REST controller for managing Member.
 */
@RestController
@RequestMapping("/api")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private SignupRepository signupRepository;

    @Inject
    private IntegrationRepository integrationRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private BillingService billingService;

    /**
     * POST  /members -> Create a new member.
     */
    @RequestMapping(value = "/members",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) throws URISyntaxException, IOException {
        log.debug("REST request to save Member : {}", member);
        if (member.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("member", "idexists", "A new member cannot already have an ID")).body(null);
        }
        Member result = memberRepository.save(member);
        boolean isMember = !result.isPaidLabor();

        // don't create users for paid labor.  They typically don't have an email address, and this just breaks too
        // many things.  They won't ever login anyway
        if (isMember) {
            createUser(member);
            // generate a bill for the new guy, so that we can send it to him.
            billingService.generateBill(member.getId(), LocalDate.now().getYear());
        }

        return ResponseEntity.created(new URI("/api/members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("member", result.getId().toString()))
            .body(result);
    }

    private void createUser(@Valid @RequestBody Member member) {
        String initial = integrationRepository.findClubManagerDefault().getApikey();
        User newUser = userService.createUserInformation(member.getEmail(), initial,
            member.getFirstName(), member.getLastName(), member.getEmail(), "en");
        newUser.setActivated(true);
        userRepository.save(newUser);

        try {
            MailingList.addMember(member, getMailingListApiKey());
        } catch (IOException e) {
            log.error("unable to update email for " + member.getName() +
                " on mailchimp.  please add " + member.getEmail() + " manually.", e);
        } catch (JSONException e) {
            log.error("unable to update email for " + member.getName() +
                " on mailchimp.  please add " + member.getEmail() + " manually.", e);
        }
    }

    private String getMailingListApiKey() {
        Integration mailchimp = integrationRepository.findOne(1L);
        return mailchimp.getApikey();
    }

    /**
     * PUT  /members -> Updates an existing member.
     */
    @RequestMapping(value = "/members",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> updateMember(@Valid @RequestBody Member member) throws URISyntaxException, IOException {
        log.debug("REST request to update Member : {}", member);
        if (member.getId() == null) {
            return createMember(member);
        }

        Member oldMemberRecord = memberRepository.findOne(member.getId());
        String oldEmail = oldMemberRecord.getEmail();
        Member result = memberRepository.save(member);
        // if the email has changed, then save the new one to the user record
        if (!oldEmail.equals(result.getEmail())) {
            User user = userRepository.findOneByEmail(oldEmail).get();
            user.setLogin(result.getEmail());
            user.setEmail(result.getEmail());
            userRepository.save(user);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("member", member.getId().toString()))
            .body(result);
    }

    /**
     * GET  /members -> get all the members.
     */
    @RequestMapping(value = "/members",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Member>> getAllMembers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Members");
        Page<Member> page = null;
        boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        if (isAdmin) {
            page = memberRepository.findAllMembersOrderByLastName(pageable);
        } else {
            page = memberRepository.findMembersOnline(pageable);
            // clear the securable fields for users, since we don't want them to see
            // birth dates, occupation, and date joined.
            for (Member member : page.getContent()) {
                clearPersonalInfoFields(member);
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Clear the personal info fields for a member.
     *
     * @param member The member to clear fields on.
     */
    private void clearPersonalInfoFields(Member member) {
        member.setAddress("");
        member.setBirthday(null);
        member.setDateJoined(null);
        member.setOccupation("");
    }

    /**
     * GET  /members/:id -> get the "id" member.
     */
    @RequestMapping(value = "/members/visible",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Member>> getVisibleMembers(Pageable pageable) throws URISyntaxException {
        Page<Member> page = null;
        if (SecurityUtils.isCurrentUserAdmin()) {
            page = memberRepository.findAllMembersOrderByLastName(pageable);
        } else {
            // get the current user's member based on their email address
            String userEmail = SecurityUtils.getCurrentUser().getUsername();
            page = memberRepository.findByEmail(userEmail, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /members/:id -> get the "id" member.
     */
    @RequestMapping(value = "/members/me",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> getUserMemberRecord(Pageable pageable) throws URISyntaxException {
        // get the current user's member based on their email address
        String userEmail = SecurityUtils.getCurrentUser().getUsername();
        Member currentLoggedInMember = memberRepository.findByEmail(userEmail);
        return Optional.ofNullable(currentLoggedInMember)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * GET  /members/:id -> get the "id" member.
     */
    @RequestMapping(value = "/members/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        log.debug("REST request to get Member : {}", id);
        Member member = memberRepository.findOne(id);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            clearPersonalInfoFields(member);
        }
        return Optional.ofNullable(member)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /members/:id -> delete the "id" member.
     * This doesn't really delete a member.  Instead, it end dates them. This is much cleaner as we keep records
     * permanantely that way.  We just need to exclude these in all queries is all.
     */
    @RequestMapping(value = "/members/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);
        Member member = memberRepository.findOne(id);

        // now clean up the external mailing list
        try {
            MailingList.deleteMember(member, getMailingListApiKey());
        } catch (IOException e) {
            log.error("unable to delete email for " + member.getName() +
                " on mailchimp.  please add " + member.getEmail() + " manually.", e);
        } catch (JSONException e) {
            log.error("unable to delete email for " + member.getName() +
                " on mailchimp.  please add " + member.getEmail() + " manually.", e);
        }

        member.setEndDate(LocalDateTime.now());
        memberRepository.saveAndFlush(member);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("member", id.toString())).build();
    }
    

    @RequestMapping("/members/list")
    public void exportMeetingSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {        
        String query = "select id, last_name, first_name, phone, email, date_joined, birthday, type, occupation, paperwork, paid, amount_due from active_members";
        String[] headerColumns = {"id", "last_name", "first_name", "phone", "email", "date_joined", "birthday", "type", "occupation", "paperwork", "paid", "amount_due"};
        int[] columnWidths = {5, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
        ExcelSqlReport report = new ExcelSqlReport(query, "currentMembers", headerColumns, columnWidths, new String[0]);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "currentMemberList.xlsx"));
    }

    @RequestMapping("/members/points")
    public void exportMemberPoints(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {        
        String query = "select last_name, first_name, points from current_year_points order by last_name";
        String[] headerColumns = {"last_name", "first_name", "points"};
        int[] columnWidths = {20, 20, 20};
        ExcelSqlReport report = new ExcelSqlReport(query, "thisYearsPoints", headerColumns, columnWidths, new String[0]);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "currentYearPoints.xlsx"));
    }
}
