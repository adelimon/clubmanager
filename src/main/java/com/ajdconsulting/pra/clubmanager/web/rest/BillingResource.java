package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.domain.MemberDues;
import com.ajdconsulting.pra.clubmanager.repository.BoardMemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDateTime;
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

    public static final int BASE_DUES_AMOUNT = 500;
    public static final int DOLLARS_PER_POINT = 20;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private BoardMemberRepository boardMemberRepository;

    @RequestMapping(value = "/billing/{memberId}/{year}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void generateBill(@PathVariable Long memberId, @PathVariable Integer year) {
        Member member = memberRepository.findOne(memberId);
        MemberBill bill = new MemberBill(member);
        bill.setYear(year);
        float billCredit = 0.00f;

        boolean isBoardMember = !(boardMemberRepository.findByMemberId(memberId, year).isEmpty());
        // member type dictates the base amount that they pay.
        float baseDuesAmount = member.getStatus().getBaseDuesAmount();

        if (isBoardMember) {
            billCredit = baseDuesAmount;
        } else {
            // everyone else pays based on $500 - ($500*number of points) until amount is zero
            float totalPoints = 0.0f;
            // when generating a bill for the current year, look at the previous year's points, hence the -1 here
            List<EarnedPoints> earnedPoints = earnedPointsRepository.findByMemberId(memberId, year-1);
            for (EarnedPoints pointsEntry : earnedPoints) {
                totalPoints += pointsEntry.getPointValue();
            }
            // calculate the amount of dues they have earned
            billCredit = (DOLLARS_PER_POINT *totalPoints);
            // but they can't go over the cap so if they are over the cap set the earned amount to the cap
            if (billCredit > baseDuesAmount) {
                billCredit = baseDuesAmount;
            }


        }
        bill.setAmount(baseDuesAmount-billCredit);
    }

    private class MemberBill {

        private LocalDateTime generatedDate;
        private int year;
        private float amount;
        private Member member;

        public MemberBill(Member member) {
            generatedDate = LocalDateTime.now();
            this.member = member;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
    }
}
