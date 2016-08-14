package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelHttpOutputStream;
import com.ajdconsulting.pra.clubmanager.data.export.excel.ExcelSqlReport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Controller
public class ExcelExportController {

    private static final String DEFAULT_FILE_NAME = "signups.xlsx";

    @RequestMapping("/exportSignups")
    public void exportSignups(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.
        String query = (
            "select name, title, point_value, cash_value, reserved, job_day, leader, date from signup_report"
        );

        String[] headerColumns = {"Name", "Job", "Point Value", "Cash Value", "Job Day", "Work Leader", "Job Date"};
        int[] columnWidths = {17, 36, 11, 10, 10, 20, 10 };
        String[] formattingColumns = {"reserved"};
        ExcelSqlReport signupReport = new ExcelSqlReport(query, "Signup", headerColumns, columnWidths, formattingColumns);
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "signups.xlsx"));
    }

    @RequestMapping("/exportMeetingSignin")
    public void exportMeetingSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String query =
            "select concat(last_name, ', ', first_name) name, mt.type status, " +
                "current_year_points, '' signature from member m, member_types mt " +
                "where m.status != 9 and mt.id = m.status";

        String[] headerColumns = {"Name", "Status", "Points", "Signature"};
        int[] columnWidths = {20, 20, 10, 65};
        ExcelSqlReport report = new ExcelSqlReport(query, "meetingSignIn", headerColumns, columnWidths);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "meetingSignIn.xlsx"));
    }

    @RequestMapping("/exportRaceDay")
    public void exportRaceDay(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String query = ("select name, title, point_value, cash_value, reserved, job_day, leader, date from signup_report_race");
        String[] headerColumns = {"Name", "Job", "Point Value", "Cash Value", "Job Day", "Work Leader", "Job Date"};
        int[] columnWidths = {17, 36, 11, 10, 10, 20, 10 };
        String[] formattingColumns = {"reserved"};
        ExcelSqlReport signupReport = new ExcelSqlReport(query, "Signup", headerColumns, columnWidths, formattingColumns);
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "raceSignups.xlsx"));
    }

}
