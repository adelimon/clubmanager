package com.ajdconsulting.pra.clubmanager.scheduled;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import com.ajdconsulting.pra.clubmanager.service.MailService;
import com.ajdconsulting.pra.clubmanager.web.rest.EarnedPointsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PointsNotificationTask {

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MailService mailService;

    private static final Logger log = LoggerFactory.getLogger(PointsNotificationTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //@Scheduled(fixedRate = 5000)
    // crontab: 0 23 * 3,4,5,6,7,8,9,10,11 0
    public void sendPointsUpdateEmail() {
        Pageable page = new PageRequest(1, 400);
        List<MemberPointsInfo> allPoints = new ArrayList<MemberPointsInfo>();

        List<Member> allMembers = memberRepository.findAll();

        for (Member member : allMembers) {
            List<EarnedPoints> points = earnedPointsRepository.findByMemberId(member.getId());
            // skip folks who have no points, are paid labor, or have an empty email
            boolean skip = ((member.isPaidLabor()) ||
                (points.size() == 0) || StringUtils.isEmpty(member.getEmail())
            );
            if (skip) continue;
            // everyone else, process normally
            MemberPointsInfo memberPointsInfo = new MemberPointsInfo(member, points);
            allPoints.add(memberPointsInfo);
            mailService.sendEmail(memberPointsInfo.getMemberEmail(), memberPointsInfo.getSubject(),
                memberPointsInfo.getPointsDetail(), false, false);
        }

    }

}
