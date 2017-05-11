package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.data.export.calendar.CalendarEvent;
import com.ajdconsulting.pra.clubmanager.data.export.calendar.ICalendar;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelHttpOutputStream;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelSqlReport;
import com.ajdconsulting.pra.clubmanager.dates.EventTimes;
import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;
import com.ajdconsulting.pra.clubmanager.repository.ScheduleDateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
            EventTimes eventTimes = new EventTimes(event);
            CalendarEvent calendarEvent = new CalendarEvent(event.generateTitle(),
                event.generateUniqueId(), eventTimes.getStart(), eventTimes.getEnd(), event.generateDescription()
            );
            calendar.add(calendarEvent);
        }

        PrintWriter writer = response.getWriter();
        writer.println(calendar.toString());
        writer.flush();
    }

    @RequestMapping("/calendar/pracalendar.xlsx")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        String sql = "select " +
            "concat(dayname(sd.date), ', ', monthname(sd.date), ' ', dayofmonth(sd.date), ' ',  year(sd.date)) date, " +
            "et.type, sd.event_name, sd.event_description from " +
            "schedule_date sd, event_type et " +
            "where et.id = sd.event_type_id and " +
            "year(sd.date) = 2017 order by sd.date";
        String[] headerColumns = {"Date", "Event Type", "Event Name", "Event Description" };
        int[] columnWidths = {20, 10, 30, 40};
        ExcelSqlReport scheduleReport = new ExcelSqlReport(sql, "PRA Schedule", headerColumns, columnWidths, new String[0], 31);
        scheduleReport.write(ExcelHttpOutputStream.getOutputStream(response, "PRASchedule.xlsx"));
    }
}
