package com.ajdconsulting.pra.clubmanager.dates;

import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by adelimon on 5/9/2017.
 */
public class EventTimes {

    private LocalDateTime start;
    private LocalDateTime end;

    public EventTimes(ScheduleDate event) {
        LocalTime startTime = LocalTime.of(
            event.getEventType().getStartHour(), event.getEventType().getStartMinute()
        );
        start = LocalDateTime.of(event.getDate(), startTime);

        LocalTime endTime = LocalTime.of(
            event.getEventType().getEndHour(), 0
        );
        end = LocalDateTime.of(event.getDate(), endTime);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
