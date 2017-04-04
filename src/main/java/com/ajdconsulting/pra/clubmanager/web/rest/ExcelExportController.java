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
        ExcelSqlReport signupReport = buildSignupReport();
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "signups.xlsx"));
    }

    @RequestMapping("/exportMeetingSignin")
    public void exportMeetingSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String query =
            "select concat(last_name, ', ', first_name) name, mt.type status, " +
                "'' signature from member m, member_types mt " +
                "where m.status != 9 and mt.id = m.status order by last_name";

        String[] headerColumns = {"Name", "Status", "Signature"};
        int[] columnWidths = {20, 20, 65};
        ExcelSqlReport report = new ExcelSqlReport(query, "meetingSignIn", headerColumns, columnWidths);
        report.write(ExcelHttpOutputStream.getOutputStream(response, "meetingSignIn.xlsx"));
    }

    @RequestMapping("/exportRaceDay")
    public void exportRaceDay(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        ExcelSqlReport signupReport = buildSignupReport();
        signupReport.write(ExcelHttpOutputStream.getOutputStream(response, "raceSignups.xlsx"));
    }

    private ExcelSqlReport buildSignupReport() throws SQLException {
        String query = ("select name, title, point_value, cash_value, reserved, meal_ticket from signup_report_race");
        String[] headerColumns = {"Name", "Job", "Point Value", "Cash Value", "Meal Ticket", "Signature" };
        int[] columnWidths = {17, 46, 11, 10, 10, 36};
        String[] formattingColumns = {"reserved"};
        return new ExcelSqlReport(query, "Signup", headerColumns, columnWidths, formattingColumns);
    }

}
