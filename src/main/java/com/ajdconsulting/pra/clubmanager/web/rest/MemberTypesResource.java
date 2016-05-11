package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.MemberTypes;
import com.ajdconsulting.pra.clubmanager.repository.MemberTypesRepository;
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
 * REST controller for managing MemberTypes.
 */
@RestController
@RequestMapping("/api")
public class MemberTypesResource {

    private final Logger log = LoggerFactory.getLogger(MemberTypesResource.class);
        
    @Inject
    private MemberTypesRepository memberTypesRepository;
    
    /**
     * POST  /memberTypess -> Create a new memberTypes.
     */
    @RequestMapping(value = "/memberTypess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberTypes> createMemberTypes(@Valid @RequestBody MemberTypes memberTypes) throws URISyntaxException {
        log.debug("REST request to save MemberTypes : {}", memberTypes);
        if (memberTypes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberTypes", "idexists", "A new memberTypes cannot already have an ID")).body(null);
        }
        MemberTypes result = memberTypesRepository.save(memberTypes);
        return ResponseEntity.created(new URI("/api/memberTypess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberTypes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /memberTypess -> Updates an existing memberTypes.
     */
    @RequestMapping(value = "/memberTypess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberTypes> updateMemberTypes(@Valid @RequestBody MemberTypes memberTypes) throws URISyntaxException {
        log.debug("REST request to update MemberTypes : {}", memberTypes);
        if (memberTypes.getId() == null) {
            return createMemberTypes(memberTypes);
        }
        MemberTypes result = memberTypesRepository.save(memberTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberTypes", memberTypes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /memberTypess -> get all the memberTypess.
     */
    @RequestMapping(value = "/memberTypess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberTypes>> getAllMemberTypess(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MemberTypess");
        Page<MemberTypes> page = memberTypesRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/memberTypess");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /memberTypess/:id -> get the "id" memberTypes.
     */
    @RequestMapping(value = "/memberTypess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberTypes> getMemberTypes(@PathVariable Long id) {
        log.debug("REST request to get MemberTypes : {}", id);
        MemberTypes memberTypes = memberTypesRepository.findOne(id);
        return Optional.ofNullable(memberTypes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /memberTypess/:id -> delete the "id" memberTypes.
     */
    @RequestMapping(value = "/memberTypess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberTypes(@PathVariable Long id) {
        log.debug("REST request to delete MemberTypes : {}", id);
        memberTypesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberTypes", id.toString())).build();
    }
}
