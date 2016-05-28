package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.SignupReport;
import com.ajdconsulting.pra.clubmanager.repository.SignupReportRepository;
import com.ajdconsulting.pra.clubmanager.web.rest.util.HeaderUtil;
import com.ajdconsulting.pra.clubmanager.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SignupReport.
 */
@RestController
@RequestMapping("/api")
public class SignupReportResource {

    private final Logger log = LoggerFactory.getLogger(SignupReportResource.class);
        
    @Inject
    private SignupReportRepository signupReportRepository;
    
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
        Page<SignupReport> page = signupReportRepository.findAll(pageable); 
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
}
