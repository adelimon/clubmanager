package com.ajdconsulting.pra.clubmanager.service;

import com.ajdconsulting.pra.clubmanager.domain.BoardMember;
import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.domain.MemberBill;
import com.ajdconsulting.pra.clubmanager.renewals.EmailContent;
import com.ajdconsulting.pra.clubmanager.repository.BoardMemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberBillRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BillingService {

    public static final int DOLLARS_PER_POINT = 24;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private BoardMemberRepository boardMemberRepository;

    @Inject
    private MemberBillRepository memberBillRepository;

    @Inject
    private MailService mailService;

    public List<Long> generateAllBills() throws IOException {
        List<Long> billIds = new ArrayList<Long>();
        // update everyone who's not a paid labor to unpaid and un renewed for the current year
        List<Member> realMembers = memberRepository.findAllRealMembers();
        for (Member m : realMembers) {
            m.setCurrentYearPaid(false);
            m.setCurrentYearRenewed(false);
            memberRepository.save(m);
        }
        memberRepository.flush();

        // now that we have that done, get all of them, and generate a bill for each one.
        // but first, figure out what year we are running billing for
        LocalDate now = LocalDate.now();
        // if it's December, then we want to run billing for the following year.  Otherwise, it's for this year.
        int year = now.getYear();
        if (now.getMonthValue() == 12) {
            year = year + 1;
        }
        for (Member m : realMembers) {
            Long billId = generateBill(m.getId(), year);
            billIds.add(billId);
        }
        return billIds;
    }

    public Long generateBill(Long memberId, int year) throws IOException {
        Member member = memberRepository.findOne(memberId);
        MemberBill bill = new MemberBill(member);
        bill.setYear(year);
        double billCredit = 0.00;

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
            // also check to see what their sign up date is and give a credit appropriately
            LocalDate joinedDate = member.getDateJoined();
            // if they joined this year, then check the date
            if (joinedDate.getYear() == year) {
                // between 7/1 and 9/30, we charge $380, a credit of $120 or 26% off.
                LocalDate creditStart = LocalDate.of(year, Month.JUNE, 30);
                LocalDate creditEnd = LocalDate.of(year, Month.OCTOBER, 1);
                if (joinedDate.isAfter(creditStart) && joinedDate.isBefore(creditEnd)) {
                    billCredit = baseDuesAmount*0.26;
                }
            } else if (joinedDate.getYear() == year-1) {
                // here they paid the prior year, and they paid on 10/1 or after, so give them a full credit
                // for the following year.
                if (joinedDate.isAfter(LocalDate.of(year-1, Month.SEPTEMBER, 30))) {
                    billCredit = baseDuesAmount;
                }
            }

        }
        bill.setAmount(baseDuesAmount-billCredit);
        EmailContent baseEmailContent = new EmailContent("memberRenewal");
        if (member.getStatus().equals("New Member")) {
            baseEmailContent = new EmailContent("newMember");
        }
        BoardMember secretary = boardMemberRepository.findByTitle( (LocalDate.now()).getYear(),
            "Secretary");
        Member secretaryMember = secretary.getMember();
        baseEmailContent.setVariables(bill, secretaryMember);
        bill.setEmailedBill(baseEmailContent.toString());
        memberBillRepository.save(bill);
        memberBillRepository.flush();
        return bill.getId();
    }

    public void sendUnsentBills(int year, boolean isDryRun) {
        List<MemberBill> bills = memberBillRepository.getUnsentBillsByYear(year);
        for (MemberBill bill : bills) {
            Member member = bill.getMember();
            if (!isDryRun) {
                mailService.sendEmail(
                    member.getEmail(), "Your " + year + "PRA membership", bill.getEmailedBill(),
                    false, true
                );
            }
            bill.setSent(true);
            member.setRenewalSent(true);
            memberRepository.save(member);
        }

    }
}
