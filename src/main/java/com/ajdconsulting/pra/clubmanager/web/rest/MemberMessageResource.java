package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.service.MailService;
import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.domain.MemberMessage;
import com.ajdconsulting.pra.clubmanager.repository.MemberMessageRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MemberMessage.
 */
@RestController
@RequestMapping("/api")
public class MemberMessageResource {

    private final Logger log = LoggerFactory.getLogger(MemberMessageResource.class);

    @Inject
    private MemberMessageRepository memberMessageRepository;

    @Inject
    private MailService mailService;

    /**
     * POST  /memberMessages -> Create a new memberMessage.
     */
    @RequestMapping(value = "/memberMessages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberMessage> createMemberMessage(@Valid @RequestBody MemberMessage memberMessage) throws URISyntaxException {
        log.debug("REST request to save MemberMessage : {}", memberMessage);
        if (memberMessage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberMessage", "idexists", "A new memberMessage cannot already have an ID")).body(null);
        }
        MemberMessage result = memberMessageRepository.save(memberMessage);
        return ResponseEntity.created(new URI("/api/memberMessages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberMessage", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/memberMessages/send{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Boolean> sendMessage(@PathVariable Long id) {
        MemberMessage message = memberMessageRepository.findOne(id);

        mailService.sendEmailToMembership(message.getSubject(), message.getMessageText());
        message.setSendDate(LocalDate.now());
        memberMessageRepository.save(message);

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    /**
     * PUT  /memberMessages -> Updates an existing memberMessage.
     */
    @RequestMapping(value = "/memberMessages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberMessage> updateMemberMessage(@Valid @RequestBody MemberMessage memberMessage) throws URISyntaxException {
        log.debug("REST request to update MemberMessage : {}", memberMessage);
        if (memberMessage.getId() == null) {
            return createMemberMessage(memberMessage);
        }
        MemberMessage result = memberMessageRepository.save(memberMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberMessage", memberMessage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /memberMessages -> get all the memberMessages.
     */
    @RequestMapping(value = "/memberMessages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberMessage>> getAllMemberMessages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MemberMessages");
        Page<MemberMessage> page = memberMessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/memberMessages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /memberMessages/:id -> get the "id" memberMessage.
     */
    @RequestMapping(value = "/memberMessages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberMessage> getMemberMessage(@PathVariable Long id) {
        log.debug("REST request to get MemberMessage : {}", id);
        MemberMessage memberMessage = memberMessageRepository.findOne(id);
        return Optional.ofNullable(memberMessage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /memberMessages/:id -> delete the "id" memberMessage.
     */
    @RequestMapping(value = "/memberMessages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberMessage(@PathVariable Long id) {
        log.debug("REST request to delete MemberMessage : {}", id);
        memberMessageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberMessage", id.toString())).build();
    }
}
