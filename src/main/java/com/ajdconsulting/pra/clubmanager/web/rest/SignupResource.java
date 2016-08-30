package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.domain.Signup;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.SignupRepository;
import com.ajdconsulting.pra.clubmanager.security.SecurityUtils;
import com.ajdconsulting.pra.clubmanager.service.MailService;
import com.ajdconsulting.pra.clubmanager.web.rest.util.HeaderUtil;
import com.ajdconsulting.pra.clubmanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Signup.
 */
@RestController
@RequestMapping("/api")
public class SignupResource {

    private final Logger log = LoggerFactory.getLogger(SignupResource.class);

    @Inject
    private SignupRepository signupRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MailService mailService;

    /**
     * POST  /signups -> Create a new signup.
     */
    @RequestMapping(value = "/signups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Signup> createSignup(@RequestBody Signup signup) throws URISyntaxException {
        log.debug("REST request to save Signup : {}", signup);
        if (signup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("signup", "idexists", "A new signup cannot already have an ID")).body(null);
        }
        Signup result = signupRepository.save(signup);
        // now that teh result is saved, reload it from the DB so we can read it for the earned points.  This is necessary
        // if someone sends in a partial request, IE one containing only the IDs.  Valid use case for doing posts
        // from links etc.
        result = signupRepository.findOne(result.getId());
        boolean isMeetingSignup = false;
        if ((signup.getScheduleDate() != null) && (signup.getScheduleDate().getEventType() != null) ){
            isMeetingSignup = "Meeting".equals(signup.getScheduleDate().getEventType().getType());
        }

        // now save an earned points record too.  This is kind of a dirty hack a roo but oh well.
        EarnedPoints signupEarnedPoints = new EarnedPoints();
        String earnedPointsDesc = "";
        boolean verified = false;
        float pointValue = 0.0f;

        signupEarnedPoints.setDate(result.getScheduleDate().getDate());
        if (!isMeetingSignup) {
            pointValue = result.getJob().getPointValue();
            earnedPointsDesc = result.getJob().getTitle();
        } else {
            // this is a meeting signup, so just say "meeting"
            earnedPointsDesc = "Meeting";
            // always verify a meeting attendance since only admins can enter it
            // anyway, and it will usually be the secretary anyway.
            verified = true;
        }
        signupEarnedPoints.setMember(result.getWorker());
        signupEarnedPoints.setEventType(result.getScheduleDate().getEventType());
        signupEarnedPoints.setDescription(earnedPointsDesc);
        signupEarnedPoints.setVerified(verified);
        signupEarnedPoints.setPointValue(pointValue);
        earnedPointsRepository.save(signupEarnedPoints);
        log.debug("Also saved a signup as a point earned record " + signupEarnedPoints.toString());

        mailService.sendSignupEmail(result);

        String uiResponse = result.getWorker().getName() + " was signed up for job " + earnedPointsDesc +
            " for the event on " + result.getScheduleDate().getDate();

        return ResponseEntity.created(new URI("/api/signups/" + result.getId()))
            .headers(HeaderUtil.createAlert(uiResponse, ""))
            .body(result);
    }

    /**
     * POST  /signups -> Create a new signup.
     */
    @RequestMapping(value = "/signups/me",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Signup> createSignupForMe(@RequestBody Signup signup) throws URISyntaxException {
        Member loggedIn = memberRepository.findByEmail(SecurityUtils.getCurrentUserLogin());
        signup.setWorker(loggedIn);
        return this.createSignup(signup);
    }


    /**
     * PUT  /signups -> Updates an existing signup.
     */
    @RequestMapping(value = "/signups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Signup> updateSignup(@RequestBody Signup signup) throws URISyntaxException {
        log.debug("REST request to update Signup : {}", signup);
        if (signup.getId() == null) {
            return createSignup(signup);
        }
        Signup result = signupRepository.save(signup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("signup", signup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signups -> get all the signups.
     */
    @RequestMapping(value = "/signups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Signup>> getAllSignups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Signups");
        Page<Signup> page = signupRepository.findFutureSignups(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/signups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /signups/:id -> get the "id" signup.
     */
    @RequestMapping(value = "/signups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Signup> getSignup(@PathVariable Long id) {
        log.debug("REST request to get Signup : {}", id);
        Signup signup = signupRepository.findOne(id);
        return Optional.ofNullable(signup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /signups/:id -> delete the "id" signup.
     */
    @RequestMapping(value = "/signups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSignup(@PathVariable Long id) {
        log.debug("REST request to delete Signup : {}", id);
        signupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("signup", id.toString())).build();
    }
}
