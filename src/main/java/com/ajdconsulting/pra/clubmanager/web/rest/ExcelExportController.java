package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.Signup;
import com.ajdconsulting.pra.clubmanager.repository.SignupRepository;
import com.mysql.jdbc.Driver;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <DESCRIBE WHAT YOUR CLASS DOES HERE AND DO NOT CHECK IN WITHOUT DOING THIS>
 *
 * @author adelimon
 * @date 5/3/2016
 * @since Advanced Security R2 Sprint 8
 */
@Controller
public class ExcelExportController {

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
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "select " +
                        "w.first_name, w.last_name, j.title, j.point_value, j.cash_value, j.reserved, j.job_day, wl.last_name leader, sd.date " +
                        "from  " +
                        "job j " +
                        "inner join schedule_date sd on sd.event_type_id = j.event_type_id " +
                        "left join signup s on s.job_id = j.id " +
                        "left join member w on w.id = s.worker_id " +
                        "left join member  wl on wl.id = j.work_leader_id " +
                        "where sd.date <= '2016-05-22' " +
                        "order by sd.date, j.title"
        );
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet signupSheet = workbook.createSheet("Signups");

        int record = 0;
        for (Map<String, Object> row : maps) {
            HSSFRow excelRow = signupSheet.createRow(record++);
            int rowIndex = 0;
            for (String key : row.keySet()) {
                String value = "";
                if (row.get(key) != null) {
                    value = row.get(key).toString();
                }
                excelRow.createCell(rowIndex).setCellValue(value);
                rowIndex++;
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("content-disposition", "filename=signups.xls");
        workbook.write(response.getOutputStream());
    }
}
