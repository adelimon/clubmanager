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

    @RequestMapping("/exportSignups")
    public void exportSignups(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.
        ExcelSqlReport signupReport = buildSignupReport();
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "signups.xlsx"));
    }

    @RequestMapping("/exportMeetingSignin")
    public void exportMeetingSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String query =
            "select concat(last_name, ', ', first_name) name, " +
                "'' signature from member m " +
                "where m.status != 9 order by last_name";

        String[] headerColumns = {"Name", "Signature"};
        int[] columnWidths = {20, 70};
        ExcelSqlReport report = new ExcelSqlReport(query, "meetingSignIn", headerColumns, columnWidths);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "meetingSignIn.xlsx"));
    }

    @RequestMapping("/exportWorkdaySignin")
    public void exportWorkdaySignin(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String query =
            "select concat(last_name, ', ', first_name) name, " +
                "'' start, '' end, '' task, '' signature from member m " +
                "where m.status != 9 order by last_name";

        String[] headerColumns = {"Name", "Start Time", "End Time", "Task(s)", "Signature"};
        int[] columnWidths = {20, 10, 10, 30, 30};
        ExcelSqlReport report = new ExcelSqlReport(query, "workdaySignin", headerColumns, columnWidths);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "workdaySignin.xlsx"));
    }

    @RequestMapping("/exportRaceDay")
    public void exportRaceDay(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        ExcelSqlReport signupReport = buildSignupReport();
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "raceSignups.xlsx"));
    }

    private ExcelSqlReport buildSignupReport() throws SQLException, IOException {
        String query = ("select name, title, job_day, point_value, cash_value, reserved, meal_ticket from signup_report");
        String[] headerColumns = {"Name", "Job", "Day", "Point Value", "Cash Value", "Meal Ticket", "Signature" };
        int[] columnWidths = {16, 29, 10, 6, 6, 6, 27};
        String[] formattingColumns = {"reserved"};
        return new ExcelSqlReport(query, "Signup", headerColumns, columnWidths, formattingColumns);
    }

}
