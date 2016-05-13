package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.PaidLabor;
import com.ajdconsulting.pra.clubmanager.repository.PaidLaborRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PaidLabor.
 */
@RestController
@RequestMapping("/api")
public class PaidLaborResource {

    private final Logger log = LoggerFactory.getLogger(PaidLaborResource.class);
        
    @Inject
    private PaidLaborRepository paidLaborRepository;
    
    /**
     * POST  /paidLabors -> Create a new paidLabor.
     */
    @RequestMapping(value = "/paidLabors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaidLabor> createPaidLabor(@Valid @RequestBody PaidLabor paidLabor) throws URISyntaxException {
        log.debug("REST request to save PaidLabor : {}", paidLabor);
        if (paidLabor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("paidLabor", "idexists", "A new paidLabor cannot already have an ID")).body(null);
        }
        PaidLabor result = paidLaborRepository.save(paidLabor);
        return ResponseEntity.created(new URI("/api/paidLabors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("paidLabor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paidLabors -> Updates an existing paidLabor.
     */
    @RequestMapping(value = "/paidLabors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaidLabor> updatePaidLabor(@Valid @RequestBody PaidLabor paidLabor) throws URISyntaxException {
        log.debug("REST request to update PaidLabor : {}", paidLabor);
        if (paidLabor.getId() == null) {
            return createPaidLabor(paidLabor);
        }
        PaidLabor result = paidLaborRepository.save(paidLabor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("paidLabor", paidLabor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paidLabors -> get all the paidLabors.
     */
    @RequestMapping(value = "/paidLabors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PaidLabor>> getAllPaidLabors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PaidLabors");
        Page<PaidLabor> page = paidLaborRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/paidLabors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /paidLabors/:id -> get the "id" paidLabor.
     */
    @RequestMapping(value = "/paidLabors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaidLabor> getPaidLabor(@PathVariable Long id) {
        log.debug("REST request to get PaidLabor : {}", id);
        PaidLabor paidLabor = paidLaborRepository.findOne(id);
        return Optional.ofNullable(paidLabor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paidLabors/:id -> delete the "id" paidLabor.
     */
    @RequestMapping(value = "/paidLabors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePaidLabor(@PathVariable Long id) {
        log.debug("REST request to delete PaidLabor : {}", id);
        paidLaborRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("paidLabor", id.toString())).build();
    }
}
