package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
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
 * REST controller for managing EarnedPoints.
 */
@RestController
@RequestMapping("/api")
public class EarnedPointsResource {

    private final Logger log = LoggerFactory.getLogger(EarnedPointsResource.class);
        
    @Inject
    private EarnedPointsRepository earnedPointsRepository;
    
    /**
     * POST  /earnedPointss -> Create a new earnedPoints.
     */
    @RequestMapping(value = "/earnedPointss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EarnedPoints> createEarnedPoints(@Valid @RequestBody EarnedPoints earnedPoints) throws URISyntaxException {
        log.debug("REST request to save EarnedPoints : {}", earnedPoints);
        if (earnedPoints.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("earnedPoints", "idexists", "A new earnedPoints cannot already have an ID")).body(null);
        }
        EarnedPoints result = earnedPointsRepository.save(earnedPoints);
        return ResponseEntity.created(new URI("/api/earnedPointss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("earnedPoints", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /earnedPointss -> Updates an existing earnedPoints.
     */
    @RequestMapping(value = "/earnedPointss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EarnedPoints> updateEarnedPoints(@Valid @RequestBody EarnedPoints earnedPoints) throws URISyntaxException {
        log.debug("REST request to update EarnedPoints : {}", earnedPoints);
        if (earnedPoints.getId() == null) {
            return createEarnedPoints(earnedPoints);
        }
        EarnedPoints result = earnedPointsRepository.save(earnedPoints);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("earnedPoints", earnedPoints.getId().toString()))
            .body(result);
    }

    /**
     * GET  /earnedPointss -> get all the earnedPointss.
     */
    @RequestMapping(value = "/earnedPointss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EarnedPoints>> getAllEarnedPointss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EarnedPointss");
        Page<EarnedPoints> page = earnedPointsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/earnedPointss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /earnedPointss/:id -> get the "id" earnedPoints.
     */
    @RequestMapping(value = "/earnedPointss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EarnedPoints> getEarnedPoints(@PathVariable Long id) {
        log.debug("REST request to get EarnedPoints : {}", id);
        EarnedPoints earnedPoints = earnedPointsRepository.findOne(id);
        return Optional.ofNullable(earnedPoints)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /earnedPointss/:id -> delete the "id" earnedPoints.
     */
    @RequestMapping(value = "/earnedPointss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEarnedPoints(@PathVariable Long id) {
        log.debug("REST request to delete EarnedPoints : {}", id);
        earnedPointsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("earnedPoints", id.toString())).build();
    }
}
