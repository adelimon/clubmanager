package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.MemberStatus;
import com.ajdconsulting.pra.clubmanager.repository.MemberStatusRepository;
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
 * REST controller for managing MemberStatus.
 */
@RestController
@RequestMapping("/api")
public class MemberStatusResource {

    private final Logger log = LoggerFactory.getLogger(MemberStatusResource.class);
        
    @Inject
    private MemberStatusRepository memberStatusRepository;
    
    /**
     * POST  /memberStatuss -> Create a new memberStatus.
     */
    @RequestMapping(value = "/memberStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberStatus> createMemberStatus(@Valid @RequestBody MemberStatus memberStatus) throws URISyntaxException {
        log.debug("REST request to save MemberStatus : {}", memberStatus);
        if (memberStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberStatus", "idexists", "A new memberStatus cannot already have an ID")).body(null);
        }
        MemberStatus result = memberStatusRepository.save(memberStatus);
        return ResponseEntity.created(new URI("/api/memberStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /memberStatuss -> Updates an existing memberStatus.
     */
    @RequestMapping(value = "/memberStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberStatus> updateMemberStatus(@Valid @RequestBody MemberStatus memberStatus) throws URISyntaxException {
        log.debug("REST request to update MemberStatus : {}", memberStatus);
        if (memberStatus.getId() == null) {
            return createMemberStatus(memberStatus);
        }
        MemberStatus result = memberStatusRepository.save(memberStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberStatus", memberStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /memberStatuss -> get all the memberStatuss.
     */
    @RequestMapping(value = "/memberStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberStatus>> getAllMemberStatuss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MemberStatuss");
        Page<MemberStatus> page = memberStatusRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/memberStatuss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /memberStatuss/:id -> get the "id" memberStatus.
     */
    @RequestMapping(value = "/memberStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberStatus> getMemberStatus(@PathVariable Long id) {
        log.debug("REST request to get MemberStatus : {}", id);
        MemberStatus memberStatus = memberStatusRepository.findOne(id);
        return Optional.ofNullable(memberStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /memberStatuss/:id -> delete the "id" memberStatus.
     */
    @RequestMapping(value = "/memberStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberStatus(@PathVariable Long id) {
        log.debug("REST request to delete MemberStatus : {}", id);
        memberStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberStatus", id.toString())).build();
    }
}
