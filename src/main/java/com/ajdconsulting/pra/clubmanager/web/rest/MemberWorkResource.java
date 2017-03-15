package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.*;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import com.ajdconsulting.pra.clubmanager.repository.MemberWorkRepository;
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
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MemberWork.
 */
@RestController
@RequestMapping("/api")
public class MemberWorkResource {

    private final Logger log = LoggerFactory.getLogger(MemberWorkResource.class);

    @Inject
    private MemberWorkRepository memberWorkRepository;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    /**
     * POST  /memberWorks -> Create a new memberWork.
     */
    @RequestMapping(value = "/memberWorks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberWork> createMemberWork(@Valid @RequestBody MemberWork memberWork) throws URISyntaxException {
        log.debug("REST request to save MemberWork : {}", memberWork);
        String email = SecurityUtils.getCurrentUser().getUsername();
        Member loggedinMember = memberRepository.findByEmail(email);
        memberWork.setMember(loggedinMember);
        if (memberWork.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberWork", "idexists", "A new memberWork cannot already have an ID")).body(null);
        }
        MemberWork result = memberWorkRepository.save(memberWork);
        EarnedPoints earnedPoints = new SelfEnteredEarnedPoints(result).toEarnedPoints();
        earnedPointsRepository.save(earnedPoints);

        return ResponseEntity.created(new URI("/api/memberWorks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberWork", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /memberWorks -> Updates an existing memberWork.
     */
    @RequestMapping(value = "/memberWorks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberWork> updateMemberWork(@Valid @RequestBody MemberWork memberWork) throws URISyntaxException {
        log.debug("REST request to update MemberWork : {}", memberWork);
        if (memberWork.getId() == null) {
            return createMemberWork(memberWork);
        }
        MemberWork result = memberWorkRepository.save(memberWork);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberWork", memberWork.getId().toString()))
            .body(result);
    }

    /**
     * GET  /memberWorks -> get all the memberWorks.
     */
    @RequestMapping(value = "/memberWorks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MemberWork> getAllMemberWorks() {
        log.debug("REST request to get all MemberWorks");
        return memberWorkRepository.findAll();
            }

    /**
     * GET  /memberWorks/:id -> get the "id" memberWork.
     */
    @RequestMapping(value = "/memberWorks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberWork> getMemberWork(@PathVariable Long id) {
        log.debug("REST request to get MemberWork : {}", id);
        MemberWork memberWork = memberWorkRepository.findOne(id);
        return Optional.ofNullable(memberWork)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /memberWorks/:id -> delete the "id" memberWork.
     */
    @RequestMapping(value = "/memberWorks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberWork(@PathVariable Long id) {
        log.debug("REST request to delete MemberWork : {}", id);
        memberWorkRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberWork", id.toString())).build();
    }
}
