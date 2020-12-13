package com.ajdconsulting.pra.clubmanager.service;

import com.ajdconsulting.pra.clubmanager.domain.BoardMember;
import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.domain.MemberBill;
import com.ajdconsulting.pra.clubmanager.renewals.EmailContent;
import com.ajdconsulting.pra.clubmanager.repository.*;
import com.lob.Lob;
import com.lob.model.Address;
import com.lob.model.Letter;
import com.lob.net.LobResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BillingService {

    public static final double DOLLARS_PER_POINT = 29.50;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private BoardMemberRepository boardMemberRepository;

    @Inject
    private MemberBillRepository memberBillRepository;

    @Inject
    private IntegrationRepository integrationRepository;

    @Inject
    private MailService mailService;

    public List<Long> generateAllBills() throws IOException {
        List<Long> billIds = new ArrayList<Long>();
        // update everyone who's not a paid labor to unpaid and un renewed for the current year
        List<Member> realMembers = memberRepository.findBillableMembers();
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
            // this is a check to prevent re-runs becaue that's annoying and in this case we don't want re-runs
            // just batches
            List<MemberBill> yearlyBills = memberBillRepository.getMemberBillYear(year, m.getId());
            if (yearlyBills.size() == 0) {
                Long billId = generateBill(m.getId(), year);
                billIds.add(billId);
            }
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
        // round to 2 decimals because we need this..sometimes.
        BigDecimal twoDecimalAmount = new BigDecimal(baseDuesAmount-billCredit);
        twoDecimalAmount.setScale(2, RoundingMode.HALF_UP);
        bill.setAmount(twoDecimalAmount.doubleValue());

        EmailContent baseEmailContent = new EmailContent("memberRenewal");
        BoardMember secretary = boardMemberRepository.findByTitle( year,"Secretary");
        Member secretaryMember = secretary.getMember();
        baseEmailContent.setVariables(bill, secretaryMember);
        bill.setEmailedBill(baseEmailContent.toString());
        memberBillRepository.save(bill);
        memberBillRepository.flush();
        return bill.getId();
    }

    public List<MemberBill> sendUnsentBills(int year, boolean isDryRun, int count) {
        List<MemberBill> bills = memberBillRepository.getUnsentBillsByYear(year);
        List<MemberBill> sentBills = new ArrayList<MemberBill>();
        int sentCount = 0;
        for (MemberBill bill : bills) {
            // if the count is reached, jump out of here
            if (sentCount >= count) {
                return sentBills;
            }
            Member member = bill.getMember();
            if (!isDryRun) {
                if (!member.getPrefersMail()) {
                    String billStatus = mailService.sendEmail(
                        member.getEmail(), "Your " + year + " PRA membership", bill.getEmailedBill(),
                        false, true
                    );
                    bill.setBillStatus(billStatus);
                } else {
                    // this is someone who prefers to get mail, so send it via the Lob service
                    String lobKey = integrationRepository.findPlatformById("lob").getApikey();
                    Lob.init(lobKey);
                    try {
                        BoardMember secretary = boardMemberRepository.findByTitle( year,
                            "Secretary");
                        Member secretaryMember = secretary.getMember();
                        LobResponse<Letter> response = new Letter.RequestBuilder()
                            .setAddressPlacement("insert_blank_page")
                            .setDescription("Your " + year + " PRA membership")
                            .setFile(bill.getEmailedBill())
                            .setColor(false)
                            .setTo(
                                new Address.RequestBuilder()
                                    .setName(member.getFirstNameLastName())
                                    .setLine1(member.getAddress())
                                    .setCity(member.getCity())
                                    .setState(member.getState())
                                    .setZip(member.getZip())
                            )
                            .setFrom(
                                new Address.RequestBuilder()
                                    // This will set the org as the first line and c/o secretary as the
                                    // second line. So even though it looks odd, it works.
                                    .setName("Palmyra Racing Association")
                                    .setCompany("c/o " + secretaryMember.getFirstNameLastName())
                                    .setLine1(secretaryMember.getAddress())
                                    .setCity(secretaryMember.getCity())
                                    .setState(secretaryMember.getState())
                                    .setZip(secretaryMember.getZip())
                            )
                            .create();
                        Letter letter = response.getResponseBody();
                        String mailedBillStatus = "";
                        if (response.getResponseCode() == HttpStatus.OK.value()) {
                            mailedBillStatus = "Mailed bill OK";
                        } else {
                            mailedBillStatus = "Mailed bill error " + response.getResponseCode();
                        }
                        bill.setBillStatus(mailedBillStatus);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            bill.setSent(true);
            member.setRenewalSent(true);
            memberRepository.saveAndFlush(member);
            memberBillRepository.saveAndFlush(bill);
            sentBills.add(bill);
            sentCount++;
        }
        return sentBills;
    }
}
