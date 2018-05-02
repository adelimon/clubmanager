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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/billing/{memberId}/{year}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String generateBill(@PathVariable Long memberId, @PathVariable Integer year) throws IOException {
        return billingService.generateBill(memberId, year);
    }

    @RequestMapping(value = "/billing/{year}/{dryRun}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void sendUnsentBills(@PathVariable Integer year, @PathVariable Boolean dryRun) {
        billingService.sendUnsentBills(year, dryRun);
    }

}
