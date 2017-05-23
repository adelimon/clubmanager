package com.ajdconsulting.pra.clubmanager.service;

import com.ajdconsulting.pra.clubmanager.dates.CurrentFiscalYear;
import com.ajdconsulting.pra.clubmanager.domain.*;
import com.ajdconsulting.pra.clubmanager.repository.BoardMemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberYearlyDuesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adelimon on 5/23/2017.
 */
@Service
@Transactional
public class MemberDuesCalculationService {

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MemberYearlyDuesRepository memberYearlyDuesRepository;

    @Inject
    private BoardMemberRepository boardMemberRepository;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    public List<MemberDues> getAllMemberDues(boolean includeSent) {
        List<MemberDues> memberDues = new ArrayList<MemberDues>();

        List<Member> allMembers = memberRepository.findAll();
        // sort the list by last name
        allMembers.sort((Member o1, Member o2) -> o1.getLastName().compareTo(o2.getLastName()));

        memberYearlyDuesRepository.deleteAll();

        for (Member member : allMembers) {
            // skip anyone who has already been sent
            if (!includeSent && member.isRenewalSent()) continue;

            // skip all paid labor, since we don't want to count them in this total
            if (member.isPaidLabor()) continue;

            MemberDues dues = getMemberDues(member);
            memberDues.add(dues);
            MemberYearlyDues yearlyDues = new MemberYearlyDues();
            yearlyDues.setId(member.getId() + CurrentFiscalYear.getFiscalYear());
            yearlyDues.setAmountDue(dues.getAmountDue());
            yearlyDues.setPoints(dues.getPoints());
            yearlyDues.setYear(CurrentFiscalYear.getFiscalYear());
            yearlyDues.setMember(member);
            memberYearlyDuesRepository.save(yearlyDues);
        }

        return memberDues;
    }

    public MemberDues getMemberDues(Member member) {
        MemberDues dues = new MemberDues(member);
        List<BoardMember> boardMembers = boardMemberRepository.findAll();

        long[] boardMemberIds = new long[boardMembers.size()];
        for (int index = 0; index < boardMemberIds.length; index++) {
            BoardMember boardMember = boardMembers.get(index);
            boolean nextYearBoard =
                (boardMember.getYear() == CurrentFiscalYear.getNextFiscalYear());
            if (nextYearBoard) {
                boardMemberIds[index] = boardMember.getMember().getId();
            }
        }

        List<EarnedPoints> earnedPoints = earnedPointsRepository.findByMemberId(
            member.getId(), CurrentFiscalYear.getFiscalYear()
        );
        float totalPoints = member.getTotalPoints(earnedPoints);
        float totalDues = member.getTotalDues(totalPoints, boardMemberIds);
        dues.setMemberId(member.getId());
        dues.setPoints(totalPoints);
        dues.setAmountDue(totalDues);
        dues.setPrefersMail(member.getPrefersMail());
        if (totalDues == 0.0) {
            member.setCurrentYearPaid(true);
            memberRepository.save(member);
        }
        return dues;
    }
}
