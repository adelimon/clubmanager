package com.ajdconsulting.pra.clubmanager.domain;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * An entity to insert earned points from a user's self-entered work.
 *
 * @author adelimon
 * @since 2017 release #2
 */
public class SelfEnteredEarnedPoints {

    private static int HOURS_PER_POINT = 3;
    private static int SECONDS_PER_HOUR = 3600;

    private EarnedPoints earnedPoints;

    /**
     * Build earned points from member work.
     * @param enteredWork
     */
    public SelfEnteredEarnedPoints(MemberWork enteredWork) {
        earnedPoints = new EarnedPoints();
        earnedPoints.setDate(enteredWork.getStart().toLocalDate());

        float points = calculatePointsFromStartEnd(enteredWork.getStart(), enteredWork.getEnd());

        earnedPoints.setDescription(
            enteredWork.getDescription() + " from " + enteredWork.getStart().toString() +
                " to " + enteredWork.getEnd().toString()
        );
        earnedPoints.setMember(enteredWork.getMember());
        EventType eventType = new EventType();
        eventType.setId(3L);
        eventType.setType("Ongoing Work");
        earnedPoints.setEventType(eventType);
        earnedPoints.setVerified(false);
        earnedPoints.setPointValue(points);
    }

    private float calculatePointsFromStartEnd(ZonedDateTime start, ZonedDateTime end) {
        Duration between = Duration.between(start, end);
        long hours = between.getSeconds() / SECONDS_PER_HOUR;
        float points = hours/HOURS_PER_POINT;
        return points;
    }

    public EarnedPoints toEarnedPoints() {
        if (earnedPoints == null) {
            throw new IllegalArgumentException("This object has to be filled out before calling this method");
        }
        return earnedPoints;
    }
}
