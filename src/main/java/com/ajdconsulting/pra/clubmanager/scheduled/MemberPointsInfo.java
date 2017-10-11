package com.ajdconsulting.pra.clubmanager.scheduled;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.domain.EventType;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.util.LineEndStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Member points information tracking for the email task.  This class will store all the information about it
 * and then the email process will use it to send the information out to members.
 *
 * @author adelimon
 * @since 2/15/2017
 */
public class MemberPointsInfo {

    private Logger log = LoggerFactory.getLogger(MemberPointsInfo.class);

    private Member member;

    private float pointsTotal;

    private List<EarnedPoints> pointsDetail;

    public MemberPointsInfo(Member member, List<EarnedPoints> pointsDetail) {
        this.member = member;
        this.pointsDetail = pointsDetail;
        for (EarnedPoints points : pointsDetail) {
            this.pointsTotal += points.getPointValue();
        }
    }

    public String getMemberEmail() {
        return member.getEmail();
    }

    public String getPointsDetail() {
        LineEndStringBuilder builder = new LineEndStringBuilder();
        builder.append(getSubject());
        builder.appendEmptyLine();
        builder.append("So far you have " + pointsTotal + " points.  They were earned as follows:");
        builder.appendEmptyLine();
        float runningTotal = 0.0f;
        for (EarnedPoints pointsRecord : pointsDetail) {
            runningTotal += pointsRecord.getPointValue();
            log.info("member id " + pointsRecord.getMember().getId());
            EventType eventType = pointsRecord.getEventType();
            String eventTypeStr = "";
            if (eventType != null) {
                eventTypeStr = eventType.getType();
            }
            String detail = pointsRecord.getDate().toString() + " - " + eventTypeStr + " - " +
                pointsRecord.getDescription() + " - " + pointsRecord.getPointValue() + " (" + runningTotal + " points total)";
            builder.append(detail);
        }
        builder.append("Please note that this email reflects points that are recorded.  If something appears to be");
        builder.append("missing, then it may not have been recorded yet.  You can check your points totals live at ");
        builder.append("any time by logging into http://apps.palmyramx.com and clicking on View your points in detail.");
        return builder.toString();
    }

    public String getSubject() {
        return LocalDate.now().getYear() + " points detail for " + member.getFirstName() + " " + member.getLastName();
    }

}
