package com.ajdconsulting.pra.clubmanager.web.rest;

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
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String contents = new String(Files.readAllBytes(Paths.get(s + "/src/main/resources/ical/template.ical")));
        List<ScheduleDate> currentYearEvents = scheduleDateRepository.findAllOrdered();
        PrintWriter writer = response.getWriter();
        LineEndStringBuilder eventOutput = new LineEndStringBuilder();

        for (ScheduleDate event : currentYearEvents) {

            LocalTime startTime = LocalTime.of(
                event.getEventType().getStartHour(), event.getEventType().getStartMinute()
            );

            LocalTime endTime = LocalTime.of(event.getEventType().getEndHour(), 00);

            LocalDateTime eventStart = LocalDateTime.of(event.getDate(), startTime);
            LocalDateTime eventEnd = LocalDateTime.of(event.getDate(), endTime);
            eventOutput.append("BEGIN:VEVENT");
            eventOutput.append("SUMMARY:" + event.getEventType().getType() + " " + event.getDate());
            eventOutput.append("UID:" + event.getEventType().getType().replaceAll(" ", "")+ event.getId());
            eventOutput.append("STATUS:CONFIRMED");
            eventOutput.append("DTSTART:" + eventStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            eventOutput.append("DTEND:" + eventEnd.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            eventOutput.append("END:VEVENT");
        }
        contents = contents.replaceAll("#EVENTS#", eventOutput.toString());
        writer.println(contents);
        writer.flush();
    }
}
