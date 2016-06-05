package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.RidingAreaStatus;
import com.ajdconsulting.pra.clubmanager.repository.RidingAreaStatusRepository;
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
 * REST controller for managing RidingAreaStatus.
 */
@RestController
@RequestMapping("/api")
public class RidingAreaStatusResource {

    private final Logger log = LoggerFactory.getLogger(RidingAreaStatusResource.class);
        
    @Inject
    private RidingAreaStatusRepository ridingAreaStatusRepository;
    
    /**
     * POST  /ridingAreaStatuss -> Create a new ridingAreaStatus.
     */
    @RequestMapping(value = "/ridingAreaStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RidingAreaStatus> createRidingAreaStatus(@Valid @RequestBody RidingAreaStatus ridingAreaStatus) throws URISyntaxException {
        log.debug("REST request to save RidingAreaStatus : {}", ridingAreaStatus);
        if (ridingAreaStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ridingAreaStatus", "idexists", "A new ridingAreaStatus cannot already have an ID")).body(null);
        }
        RidingAreaStatus result = ridingAreaStatusRepository.save(ridingAreaStatus);
        return ResponseEntity.created(new URI("/api/ridingAreaStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ridingAreaStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ridingAreaStatuss -> Updates an existing ridingAreaStatus.
     */
    @RequestMapping(value = "/ridingAreaStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RidingAreaStatus> updateRidingAreaStatus(@Valid @RequestBody RidingAreaStatus ridingAreaStatus) throws URISyntaxException {
        log.debug("REST request to update RidingAreaStatus : {}", ridingAreaStatus);
        if (ridingAreaStatus.getId() == null) {
            return createRidingAreaStatus(ridingAreaStatus);
        }
        RidingAreaStatus result = ridingAreaStatusRepository.save(ridingAreaStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ridingAreaStatus", ridingAreaStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ridingAreaStatuss -> get all the ridingAreaStatuss.
     */
    @RequestMapping(value = "/ridingAreaStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RidingAreaStatus>> getAllRidingAreaStatuss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RidingAreaStatuss");
        Page<RidingAreaStatus> page = ridingAreaStatusRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ridingAreaStatuss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ridingAreaStatuss/:id -> get the "id" ridingAreaStatus.
     */
    @RequestMapping(value = "/ridingAreaStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RidingAreaStatus> getRidingAreaStatus(@PathVariable Long id) {
        log.debug("REST request to get RidingAreaStatus : {}", id);
        RidingAreaStatus ridingAreaStatus = ridingAreaStatusRepository.findOne(id);
        return Optional.ofNullable(ridingAreaStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ridingAreaStatuss/:id -> delete the "id" ridingAreaStatus.
     */
    @RequestMapping(value = "/ridingAreaStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRidingAreaStatus(@PathVariable Long id) {
        log.debug("REST request to delete RidingAreaStatus : {}", id);
        ridingAreaStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ridingAreaStatus", id.toString())).build();
    }
}
