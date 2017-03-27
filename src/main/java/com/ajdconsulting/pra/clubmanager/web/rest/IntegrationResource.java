package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.Integration;
import com.ajdconsulting.pra.clubmanager.repository.IntegrationRepository;
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
 * REST controller for managing Integration.
 */
@RestController
@RequestMapping("/api")
public class IntegrationResource {

    private final Logger log = LoggerFactory.getLogger(IntegrationResource.class);
        
    @Inject
    private IntegrationRepository integrationRepository;
    
    /**
     * POST  /integrations -> Create a new integration.
     */
    @RequestMapping(value = "/integrations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Integration> createIntegration(@Valid @RequestBody Integration integration) throws URISyntaxException {
        log.debug("REST request to save Integration : {}", integration);
        if (integration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("integration", "idexists", "A new integration cannot already have an ID")).body(null);
        }
        Integration result = integrationRepository.save(integration);
        return ResponseEntity.created(new URI("/api/integrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("integration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /integrations -> Updates an existing integration.
     */
    @RequestMapping(value = "/integrations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Integration> updateIntegration(@Valid @RequestBody Integration integration) throws URISyntaxException {
        log.debug("REST request to update Integration : {}", integration);
        if (integration.getId() == null) {
            return createIntegration(integration);
        }
        Integration result = integrationRepository.save(integration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("integration", integration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /integrations -> get all the integrations.
     */
    @RequestMapping(value = "/integrations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Integration>> getAllIntegrations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Integrations");
        Page<Integration> page = integrationRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/integrations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /integrations/:id -> get the "id" integration.
     */
    @RequestMapping(value = "/integrations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Integration> getIntegration(@PathVariable Long id) {
        log.debug("REST request to get Integration : {}", id);
        Integration integration = integrationRepository.findOne(id);
        return Optional.ofNullable(integration)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /integrations/:id -> delete the "id" integration.
     */
    @RequestMapping(value = "/integrations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIntegration(@PathVariable Long id) {
        log.debug("REST request to delete Integration : {}", id);
        integrationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("integration", id.toString())).build();
    }
}
