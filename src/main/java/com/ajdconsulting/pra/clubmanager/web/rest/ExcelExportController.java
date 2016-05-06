package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.data.export.excel.GenericExcelSheet;
import com.mysql.jdbc.Driver;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class ExcelExportController {

    private static final String DEFAULT_FILE_NAME = "signups.xlsx";

    @RequestMapping("/exportSignups")
    public void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriver(new Driver());
        dataSource.setUrl("jdbc:mysql://localhost/clubmanager");
        dataSource.setUsername("root");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // hacky way of escaping this input against potential mucking around and attempts at Sea Quill
        // hot beef injections
        String raceWeek = "'"+request.getParameter("raceWeekDate")+"' ";

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
            "select " +
            "concat(w.first_name,' ', w.last_name) name, j.title, j.point_value, j.cash_value, j.reserved, j.job_day, wl.last_name leader, sd.date " +
            "from  " +
            "job j " +
            "inner join schedule_date sd on sd.event_type_id = j.event_type_id " +
            "left join signup s on s.job_id = j.id " +
            "left join member w on w.id = s.worker_id " +
            "left join member  wl on wl.id = j.work_leader_id " +
            "where sd.date <= " + raceWeek +
            "order by sd.date, j.title"
        );
        Workbook workbook = new XSSFWorkbook();
        GenericExcelSheet signupSheet = new GenericExcelSheet(workbook, "Signups");
        for (Map<String, Object> row : maps) {
            Row excelRow = signupSheet.createRow();
            for (String key : row.keySet()) {
                boolean reserved = Boolean.parseBoolean(row.getOrDefault("reserved", "false").toString());
                String value = "";
                if (row.get(key) != null) {
                    value = row.get(key).toString();
                }
                signupSheet.createCell(excelRow, value, reserved);
            }
        }
        writeExcelToResponse(response, workbook);
    }

    private void writeExcelToResponse(HttpServletResponse response, Workbook workbook) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("content-disposition", "filename=" + DEFAULT_FILE_NAME);
        workbook.write(response.getOutputStream());
    }

}
