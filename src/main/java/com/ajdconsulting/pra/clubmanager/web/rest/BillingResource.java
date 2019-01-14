package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.*;
import com.ajdconsulting.pra.clubmanager.renewals.EmailContent;
import com.ajdconsulting.pra.clubmanager.repository.BoardMemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberBillRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.service.BillingService;
import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

/**
 * Billing for PRA members.
 *
 * @author adelimon
 */
@RestController
@RequestMapping("/api")
public class BillingResource {


    @Inject
    public BillingService billingService;

    @RequestMapping(value = "/billing/send/{memberId}/{year}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JSONObject> generateBill(@PathVariable Long memberId, @PathVariable Integer year) throws IOException {
        Long billId = billingService.generateBill(memberId, year);
        return new ResponseEntity<JSONObject>(new JSONObject(billId), HttpStatus.OK);
    }

    @RequestMapping(value = "/billing/{year}/{dryRun}/{count}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberBill>> sendUnsentBills(@PathVariable Integer year, @PathVariable Boolean dryRun, @PathVariable Integer count) {
        if (count == null) {
            count = 10;
        }
        List<MemberBill> sendBills = billingService.sendUnsentBills(year, dryRun, count);
        return new ResponseEntity<List<MemberBill>>(sendBills, HttpStatus.OK);
    }

    @RequestMapping(value = "/billing/run",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<JSONObject> generateYearlyBills() throws IOException {
        List<Long> page = billingService.generateAllBills();
        return new ResponseEntity<JSONObject>(new JSONObject(page), HttpStatus.OK);
    }

}
