package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.config.JHipsterProperties;
import com.ajdconsulting.pra.clubmanager.data.export.excel.QueryResult;
import com.ajdconsulting.pra.clubmanager.data.export.excel.StripedSingleSheetWorkbook;
import com.google.common.util.concurrent.Striped;
import com.mysql.jdbc.Driver;
import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelExportController {

    private static final String DEFAULT_FILE_NAME = "signups.xlsx";

    @RequestMapping("/exportSignups")
    public void exportSignups(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.
        QueryResult queryResult = new QueryResult(
                "select " +
                "concat(w.first_name,' ', w.last_name) name, j.title, j.point_value, j.cash_value, j.reserved, j.job_day, wl.last_name leader, sd.date " +
                "from  " +
                "job j " +
                "inner join schedule_date sd on sd.event_type_id = j.event_type_id " +
                "left join signup s on s.job_id = j.id " +
                "left join member w on w.id = s.worker_id " +
                "left join member  wl on wl.id = j.work_leader_id " +
                "where sd.date <= (select max(date) from schedule_date where date > now() and week(date)-2 <= week(now()))" +
                "order by date, title"
        );
        StripedSingleSheetWorkbook signupSheet = new StripedSingleSheetWorkbook("Signups");
        String[] headerColumns = {"Name", "Job", "Point Value", "Cash Value", "Job Day", "Work Leader", "Job Date"};
        signupSheet.addHeader(headerColumns);
        List<Map<String, Object>> umeshDengale = queryResult.getUmeshDengale();
        for (Map<String, Object> row : umeshDengale) {
            Row excelRow = signupSheet.createRow();
            // check if the job is reserved, this is used later to bold the row if that is in fact true
            boolean reserved = false;
            if (row.get("reserved") != null) {
                reserved = Boolean.parseBoolean(row.get("reserved").toString());
            }
            for (String key : row.keySet()) {
                // since we are using reserved to bold the row we can ignore it when it comes up as an
                // attribute
                if (!"reserved".equals(key)) {
                    String value = "";
                    if (row.get(key) != null) {
                        value = row.get(key).toString();
                    }
                    signupSheet.createCell(excelRow, value, reserved);
                }
            }
        }
        writeExcelToResponse(response, signupSheet, DEFAULT_FILE_NAME);
    }


    @RequestMapping("/exportMeetingSignin")
    public void exportMeetingSignIn(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        QueryResult result = new QueryResult(
            "select concat(last_name, ', ', first_name) name, current_year_points, '' signature from member"
        );
        StripedSingleSheetWorkbook memberWorkList = new StripedSingleSheetWorkbook("meetingSignIn");
        String[] headerColumns = {"Name", "Points", "Signature"};
        memberWorkList.addHeader(headerColumns);
        List<Map<String, Object>> umeshDengale = result.getUmeshDengale();
        for (Map<String, Object> row : umeshDengale) {
            Row excelRow = memberWorkList.createRow();
            for (String key : row.keySet()) {
                String value = "";
                if (row.get(key) != null) {
                    value = row.get(key).toString();
                }
                memberWorkList.createCell(excelRow, value, false);
            }
        }
        writeExcelToResponse(response, memberWorkList, "meetingSignin.xlsx");
    }

    private void writeExcelToResponse(HttpServletResponse response, StripedSingleSheetWorkbook workbook,
        String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("content-disposition", "filename=" + DEFAULT_FILE_NAME);
        workbook.write(response.getOutputStream());
    }

}
