package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.EventType;
import com.ajdconsulting.pra.clubmanager.repository.EventTypeRepository;
import com.ajdconsulting.pra.clubmanager.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing EventType.
 */
@RestController
@RequestMapping("/api")
public class EventTypeResource {

    private final Logger log = LoggerFactory.getLogger(EventTypeResource.class);
        
    @Inject
    private EventTypeRepository eventTypeRepository;
    
    /**
     * POST  /eventTypes -> Create a new eventType.
     */
    @RequestMapping(value = "/eventTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventType> createEventType(@Valid @RequestBody EventType eventType) throws URISyntaxException {
        log.debug("REST request to save EventType : {}", eventType);
        if (eventType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("eventType", "idexists", "A new eventType cannot already have an ID")).body(null);
        }
        EventType result = eventTypeRepository.save(eventType);
        return ResponseEntity.created(new URI("/api/eventTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("eventType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /eventTypes -> Updates an existing eventType.
     */
    @RequestMapping(value = "/eventTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventType> updateEventType(@Valid @RequestBody EventType eventType) throws URISyntaxException {
        log.debug("REST request to update EventType : {}", eventType);
        if (eventType.getId() == null) {
            return createEventType(eventType);
        }
        EventType result = eventTypeRepository.save(eventType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("eventType", eventType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /eventTypes -> get all the eventTypes.
     */
    @RequestMapping(value = "/eventTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EventType> getAllEventTypes() {
        log.debug("REST request to get all EventTypes");
        return eventTypeRepository.findAll();
            }

    /**
     * GET  /eventTypes/:id -> get the "id" eventType.
     */
    @RequestMapping(value = "/eventTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventType> getEventType(@PathVariable Long id) {
        log.debug("REST request to get EventType : {}", id);
        EventType eventType = eventTypeRepository.findOne(id);
        return Optional.ofNullable(eventType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /eventTypes/:id -> delete the "id" eventType.
     */
    @RequestMapping(value = "/eventTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        log.debug("REST request to delete EventType : {}", id);
        eventTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("eventType", id.toString())).build();
    }
}
