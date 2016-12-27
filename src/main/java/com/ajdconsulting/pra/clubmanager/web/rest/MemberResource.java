package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.*;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.SignupRepository;
import com.ajdconsulting.pra.clubmanager.security.AuthoritiesConstants;
import com.ajdconsulting.pra.clubmanager.security.SecurityUtils;
import com.ajdconsulting.pra.clubmanager.web.rest.util.HeaderUtil;
import com.ajdconsulting.pra.clubmanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    /**
     * POST  /members -> Create a new member.
     */
    @RequestMapping(value = "/members",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) throws URISyntaxException {
        log.debug("REST request to save Member : {}", member);
        if (member.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("member", "idexists", "A new member cannot already have an ID")).body(null);
        }
        member.setActive(false);
        MemberTypes memberType = new MemberTypes();
        memberType.setId(9L);
        member.setStatus(memberType);
        Member result = memberRepository.save(member);
        return ResponseEntity.created(new URI("/api/members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("member", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /members -> Updates an existing member.
     */
    @RequestMapping(value = "/members",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> updateMember(@Valid @RequestBody Member member) throws URISyntaxException {
        log.debug("REST request to update Member : {}", member);
        if (member.getId() == null) {
            return createMember(member);
        }
        Member result = memberRepository.save(member);
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
     */
    @RequestMapping(value = "/members/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);
        // delete all earned points for this member first
        List<EarnedPoints> allMemberPoints = earnedPointsRepository.findByMemberId(id);
        for (EarnedPoints points : allMemberPoints) {
            earnedPointsRepository.delete(points);
        }
        List<Signup> workerSignups = signupRepository.findByWorkerId(id);
        for (Signup signup : workerSignups) {
            signupRepository.delete(signup);
        }
        memberRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("member", id.toString())).build();
    }

    /**
     * GET  /members -> get all the members.
     */
    @RequestMapping(value = "/members/dues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberDues>> getAllMemberDues(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Members");
        List<MemberDues> memberDues = new ArrayList<MemberDues>();
        List<Member> allMembers = memberRepository.findAll();
        // sort the list by last name
        allMembers.sort((Member o1, Member o2) -> o1.getLastName().compareTo(o2.getLastName()));

        for (Member member : allMembers) {
            // skip all paid labor, since we don't want to count them in this total
            if (member.isPaidLabor()) continue;

            MemberDues dues = new MemberDues(member);

            List<EarnedPoints> earnedPoints = earnedPointsRepository.findByMemberId(member.getId());
            float totalPoints = member.getTotalPoints(earnedPoints);
            float totalDues = member.getTotalDues(totalPoints);

            dues.setPoints(totalPoints);
            dues.setAmountDue(totalDues);
            memberDues.add(dues);
        }
        boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        Page<MemberDues> page = new PageImpl<MemberDues>(memberDues);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
