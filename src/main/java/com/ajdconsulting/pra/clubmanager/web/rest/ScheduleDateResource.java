package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;
import com.ajdconsulting.pra.clubmanager.repository.ScheduleDateRepository;
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
 * REST controller for managing ScheduleDate.
 */
@RestController
@RequestMapping("/api")
public class ScheduleDateResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleDateResource.class);

    @Inject
    private ScheduleDateRepository scheduleDateRepository;

    /**
     * POST  /scheduleDates -> Create a new scheduleDate.
     */
    @RequestMapping(value = "/scheduleDates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleDate> createScheduleDate(@Valid @RequestBody ScheduleDate scheduleDate) throws URISyntaxException {
        log.debug("REST request to save ScheduleDate : {}", scheduleDate);
        if (scheduleDate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scheduleDate", "idexists", "A new scheduleDate cannot already have an ID")).body(null);
        }
        ScheduleDate result = scheduleDateRepository.save(scheduleDate);
        return ResponseEntity.created(new URI("/api/scheduleDates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scheduleDate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scheduleDates -> Updates an existing scheduleDate.
     */
    @RequestMapping(value = "/scheduleDates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleDate> updateScheduleDate(@Valid @RequestBody ScheduleDate scheduleDate) throws URISyntaxException {
        log.debug("REST request to update ScheduleDate : {}", scheduleDate);
        if (scheduleDate.getId() == null) {
            return createScheduleDate(scheduleDate);
        }
        ScheduleDate result = scheduleDateRepository.save(scheduleDate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scheduleDate", scheduleDate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scheduleDates -> get all the scheduleDates.
     */
    @RequestMapping(value = "/scheduleDates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ScheduleDate>> getAllScheduleDates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScheduleDates");
        Page<ScheduleDate> page = scheduleDateRepository.findAllOrdered(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scheduleDates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scheduleDates -> get all the scheduleDates.
     */
    @RequestMapping(value = "/scheduleDates/future",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ScheduleDate>> getFutureDates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScheduleDates");
        Page<ScheduleDate> page = scheduleDateRepository.findAllFutureDates(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scheduleDates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scheduleDates/:id -> get the "id" scheduleDate.
     */
    @RequestMapping(value = "/scheduleDates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleDate> getScheduleDate(@PathVariable Long id) {
        log.debug("REST request to get ScheduleDate : {}", id);
        ScheduleDate scheduleDate = scheduleDateRepository.findOne(id);
        return Optional.ofNullable(scheduleDate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scheduleDates/:id -> delete the "id" scheduleDate.
     */
    @RequestMapping(value = "/scheduleDates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScheduleDate(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleDate : {}", id);
        scheduleDateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scheduleDate", id.toString())).build();
    }
}
