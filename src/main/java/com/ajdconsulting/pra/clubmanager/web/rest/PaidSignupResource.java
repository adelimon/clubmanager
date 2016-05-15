package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.PaidSignup;
import com.ajdconsulting.pra.clubmanager.repository.PaidSignupRepository;
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
 * REST controller for managing PaidSignup.
 */
@RestController
@RequestMapping("/api")
public class PaidSignupResource {

    private final Logger log = LoggerFactory.getLogger(PaidSignupResource.class);
        
    @Inject
    private PaidSignupRepository paidSignupRepository;
    
    /**
     * POST  /paidSignups -> Create a new paidSignup.
     */
    @RequestMapping(value = "/paidSignups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaidSignup> createPaidSignup(@RequestBody PaidSignup paidSignup) throws URISyntaxException {
        log.debug("REST request to save PaidSignup : {}", paidSignup);
        if (paidSignup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("paidSignup", "idexists", "A new paidSignup cannot already have an ID")).body(null);
        }
        PaidSignup result = paidSignupRepository.save(paidSignup);
        return ResponseEntity.created(new URI("/api/paidSignups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("paidSignup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paidSignups -> Updates an existing paidSignup.
     */
    @RequestMapping(value = "/paidSignups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaidSignup> updatePaidSignup(@RequestBody PaidSignup paidSignup) throws URISyntaxException {
        log.debug("REST request to update PaidSignup : {}", paidSignup);
        if (paidSignup.getId() == null) {
            return createPaidSignup(paidSignup);
        }
        PaidSignup result = paidSignupRepository.save(paidSignup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("paidSignup", paidSignup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paidSignups -> get all the paidSignups.
     */
    @RequestMapping(value = "/paidSignups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PaidSignup>> getAllPaidSignups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PaidSignups");
        Page<PaidSignup> page = paidSignupRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/paidSignups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /paidSignups/:id -> get the "id" paidSignup.
     */
    @RequestMapping(value = "/paidSignups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaidSignup> getPaidSignup(@PathVariable Long id) {
        log.debug("REST request to get PaidSignup : {}", id);
        PaidSignup paidSignup = paidSignupRepository.findOne(id);
        return Optional.ofNullable(paidSignup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paidSignups/:id -> delete the "id" paidSignup.
     */
    @RequestMapping(value = "/paidSignups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePaidSignup(@PathVariable Long id) {
        log.debug("REST request to delete PaidSignup : {}", id);
        paidSignupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("paidSignup", id.toString())).build();
    }
}
