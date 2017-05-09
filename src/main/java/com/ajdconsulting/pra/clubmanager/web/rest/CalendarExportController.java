package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.data.export.calendar.CalendarEvent;
import com.ajdconsulting.pra.clubmanager.data.export.calendar.ICalendar;
import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;
import com.ajdconsulting.pra.clubmanager.repository.ScheduleDateRepository;
import com.ajdconsulting.pra.clubmanager.util.LineEndStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller with endpoints to export a calendar from the events stored in the app.
 *
 * @author adelimon
 * @since 5/4/2017
 */
@Controller
public class CalendarExportController {

    @Inject
    private ScheduleDateRepository scheduleDateRepository;

    @RequestMapping("/calendar/pracalendar")
    public void exportIcal(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<ScheduleDate> currentYearEvents = scheduleDateRepository.findAllOrdered();
        ICalendar calendar = new ICalendar("/src/main/resources/ical/template.ical");

        for (ScheduleDate event : currentYearEvents) {

            LocalTime startTime = LocalTime.of(
                event.getEventType().getStartHour(), event.getEventType().getStartMinute()
            );
            LocalTime endTime = LocalTime.of(event.getEventType().getEndHour(), 00);
            LocalDateTime eventStart = LocalDateTime.of(event.getDate(), startTime);
            LocalDateTime eventEnd = LocalDateTime.of(event.getDate(), endTime);

            CalendarEvent calendarEvent = new CalendarEvent(
                event.getEventType().getType() + " " + event.getDate(),
                event.getEventType().getType().replaceAll(" ", "")+ event.getId(),
                eventStart, eventEnd
            );
            calendar.add(calendarEvent);
        }

        PrintWriter writer = response.getWriter();
        //response.setContentType("text/calendar");
        writer.println(calendar.toString());
        writer.flush();
    }
}
