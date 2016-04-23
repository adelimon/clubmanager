package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.MemberNote;
import com.ajdconsulting.pra.clubmanager.repository.MemberNoteRepository;
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
 * REST controller for managing MemberNote.
 */
@RestController
@RequestMapping("/api")
public class MemberNoteResource {

    private final Logger log = LoggerFactory.getLogger(MemberNoteResource.class);
        
    @Inject
    private MemberNoteRepository memberNoteRepository;
    
    /**
     * POST  /memberNotes -> Create a new memberNote.
     */
    @RequestMapping(value = "/memberNotes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberNote> createMemberNote(@Valid @RequestBody MemberNote memberNote) throws URISyntaxException {
        log.debug("REST request to save MemberNote : {}", memberNote);
        if (memberNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberNote", "idexists", "A new memberNote cannot already have an ID")).body(null);
        }
        MemberNote result = memberNoteRepository.save(memberNote);
        return ResponseEntity.created(new URI("/api/memberNotes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /memberNotes -> Updates an existing memberNote.
     */
    @RequestMapping(value = "/memberNotes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberNote> updateMemberNote(@Valid @RequestBody MemberNote memberNote) throws URISyntaxException {
        log.debug("REST request to update MemberNote : {}", memberNote);
        if (memberNote.getId() == null) {
            return createMemberNote(memberNote);
        }
        MemberNote result = memberNoteRepository.save(memberNote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberNote", memberNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /memberNotes -> get all the memberNotes.
     */
    @RequestMapping(value = "/memberNotes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberNote>> getAllMemberNotes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MemberNotes");
        Page<MemberNote> page = memberNoteRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/memberNotes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /memberNotes/:id -> get the "id" memberNote.
     */
    @RequestMapping(value = "/memberNotes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberNote> getMemberNote(@PathVariable Long id) {
        log.debug("REST request to get MemberNote : {}", id);
        MemberNote memberNote = memberNoteRepository.findOne(id);
        return Optional.ofNullable(memberNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /memberNotes/:id -> delete the "id" memberNote.
     */
    @RequestMapping(value = "/memberNotes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberNote(@PathVariable Long id) {
        log.debug("REST request to delete MemberNote : {}", id);
        memberNoteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberNote", id.toString())).build();
    }
}
