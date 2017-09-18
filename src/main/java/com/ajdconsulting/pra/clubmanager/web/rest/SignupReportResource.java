package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelHttpOutputStream;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelSqlReport;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.domain.Signup;
import com.ajdconsulting.pra.clubmanager.domain.SignupReport;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.SignupReportRepository;
import com.ajdconsulting.pra.clubmanager.web.rest.util.HeaderUtil;
import com.ajdconsulting.pra.clubmanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;

/**
 * REST controller for managing SignupReport.
 */
@RestController
@RequestMapping("/api")
public class SignupReportResource {

    private final Logger log = LoggerFactory.getLogger(SignupReportResource.class);

    @Inject
    private SignupReportRepository signupReportRepository;

    @Inject
    private MemberRepository memberRepository;

    /**
     * POST  /signupReports -> Create a new signupReport.
     */
    @RequestMapping(value = "/signupReports",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignupReport> createSignupReport(@RequestBody SignupReport signupReport) throws URISyntaxException {
        log.debug("REST request to save SignupReport : {}", signupReport);
        if (signupReport.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signupReport", "idexists", "A new signupReport cannot already have an ID")).body(null);
        }
        SignupReport result = signupReportRepository.save(signupReport);
        return ResponseEntity.created(new URI("/api/signupReports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("signupReport", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signupReports -> Updates an existing signupReport.
     */
    @RequestMapping(value = "/signupReports",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignupReport> updateSignupReport(@RequestBody SignupReport signupReport) throws URISyntaxException {
        log.debug("REST request to update SignupReport : {}", signupReport);
        if (signupReport.getId() == null) {
            return createSignupReport(signupReport);
        }
        SignupReport result = signupReportRepository.save(signupReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("signupReport", signupReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signupReports -> get all the signupReports.
     */
    @RequestMapping(value = "/signupReports",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SignupReport>> getAllSignupReports(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SignupReports");
        Page<SignupReport> page = signupReportRepository.findAllOrdered(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/signupReports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /signupReports/:id -> get the "id" signupReport.
     */
    @RequestMapping(value = "/signupReports/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SignupReport> getSignupReport(@PathVariable Long id) {
        log.debug("REST request to get SignupReport : {}", id);
        SignupReport signupReport = signupReportRepository.findOne(id);
        return Optional.ofNullable(signupReport)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /signupReports/:id -> delete the "id" signupReport.
     */
    @RequestMapping(value = "/signupReports/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSignupReport(@PathVariable Long id) {
        log.debug("REST request to delete SignupReport : {}", id);
        signupReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("signupReport", id.toString())).build();
    }

    @RequestMapping("/signupReports/workday/excel")
    public void exportWorkdaySignin(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String[] headerColumns = {"Name", "Start Time", "End Time", "Task(s)", "Signature"};
        int[] columnWidths = {20, 10, 10, 30, 30};
        List<Member> members = memberRepository.findAllMembersOrderByLastName();
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        for (Member member : members) {
            if (!member.isPaidLabor()) {
                Map<String, Object> fields = new HashMap<String, Object>();
                fields.put("Name", member.getName());
                fields.put("Start Time", "");
                fields.put("End Time", "");
                fields.put("Task(s)", "");
                fields.put("Signature", "");
                dataMap.add(fields);
            }
        }
        ExcelSqlReport report = new ExcelSqlReport(dataMap, "workdaySignin",headerColumns, columnWidths, new String[0]);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "workdaySignin.xlsx"));
    }

    @RequestMapping("/signupReports/meeting/excel")
    public void exportMeetingSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String[] headerColumns = {"Name", "Signature"};
        int[] columnWidths = {20, 70};
        List<Member> members = memberRepository.findAllMembersOrderByLastName();
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        for (Member member : members) {
            if (!member.isPaidLabor()) {
                Map<String, Object> fields = new HashMap<String, Object>();
                fields.put("Name", member.getName());
                fields.put("Signature", "");
                dataMap.add(fields);
            }
        }
        ExcelSqlReport report = new ExcelSqlReport(dataMap, "meetingSignIn", headerColumns, columnWidths, new String[0]);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "meetingSignIn.xlsx"));
    }

    @RequestMapping("/signupReports/race/excel")
    public void exportSignups(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.
        ExcelSqlReport signupReport = buildSignupReport();
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "signups.xlsx"));
    }

    private ExcelSqlReport buildSignupReport() throws SQLException, IOException {
        String[] headerColumns = {"Name", "Job", "Day", "Point Value", "Cash Value", "Meal Ticket", "Signature" };
        int[] columnWidths = {16, 29, 10, 6, 6, 6, 27};
        String[] formattingColumns = {"reserved"};
        List<SignupReport> signupReports = signupReportRepository.findAll();
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        for(SignupReport signup : signupReports) {
            Map<String, Object> fields = new HashMap<String, Object>();
            fields.put("Name", signup.getName());
            fields.put("Job", signup.getTitle());
            fields.put("Day", signup.getJobDay());
            fields.put("Point Value", signup.getPointValue());
            fields.put("Cash Value", signup.getCashValue());
            fields.put("Meal Ticket", signup.getMealTicket());
            fields.put("Signature", "");
            fields.put("reserved", signup.getReserved());
            dataMap.add(fields);
        }
        return new ExcelSqlReport(dataMap, "Signup", headerColumns, columnWidths, formattingColumns);
    }
}
