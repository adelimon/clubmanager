package com.ajdconsulting.pra.clubmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.BoardMember;
import com.ajdconsulting.pra.clubmanager.repository.BoardMemberRepository;
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
 * REST controller for managing BoardMember.
 */
@RestController
@RequestMapping("/api")
public class BoardMemberResource {

    private final Logger log = LoggerFactory.getLogger(BoardMemberResource.class);
        
    @Inject
    private BoardMemberRepository boardMemberRepository;
    
    /**
     * POST  /boardMembers -> Create a new boardMember.
     */
    @RequestMapping(value = "/boardMembers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BoardMember> createBoardMember(@Valid @RequestBody BoardMember boardMember) throws URISyntaxException {
        log.debug("REST request to save BoardMember : {}", boardMember);
        if (boardMember.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("boardMember", "idexists", "A new boardMember cannot already have an ID")).body(null);
        }
        BoardMember result = boardMemberRepository.save(boardMember);
        return ResponseEntity.created(new URI("/api/boardMembers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("boardMember", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boardMembers -> Updates an existing boardMember.
     */
    @RequestMapping(value = "/boardMembers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BoardMember> updateBoardMember(@Valid @RequestBody BoardMember boardMember) throws URISyntaxException {
        log.debug("REST request to update BoardMember : {}", boardMember);
        if (boardMember.getId() == null) {
            return createBoardMember(boardMember);
        }
        BoardMember result = boardMemberRepository.save(boardMember);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("boardMember", boardMember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boardMembers -> get all the boardMembers.
     */
    @RequestMapping(value = "/boardMembers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BoardMember> getAllBoardMembers() {
        log.debug("REST request to get all BoardMembers");
        return boardMemberRepository.findAll();
            }

    /**
     * GET  /boardMembers/:id -> get the "id" boardMember.
     */
    @RequestMapping(value = "/boardMembers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BoardMember> getBoardMember(@PathVariable Long id) {
        log.debug("REST request to get BoardMember : {}", id);
        BoardMember boardMember = boardMemberRepository.findOne(id);
        return Optional.ofNullable(boardMember)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /boardMembers/:id -> delete the "id" boardMember.
     */
    @RequestMapping(value = "/boardMembers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBoardMember(@PathVariable Long id) {
        log.debug("REST request to delete BoardMember : {}", id);
        boardMemberRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("boardMember", id.toString())).build();
    }
}
