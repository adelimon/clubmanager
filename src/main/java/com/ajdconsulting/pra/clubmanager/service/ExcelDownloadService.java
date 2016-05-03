package com.ajdconsulting.pra.clubmanager.service;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
@Transactional
public class ExcelDownloadService extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook workbook,
        HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        // http://www.mkyong.com/spring-mvc/spring-mvc-export-data-to-excel-file-via-abstractexcelview/
        Map<String, String> signupData = (Map<String,String>)map.get("signupData");
        HSSFSheet signup = workbook.createSheet("Signup");

        HSSFRow header = signup.createRow(0);
        header.createCell(0).setCellValue("header 1");

        int rowNum = 1;
        for (Map.Entry<String, String> entry : signupData.entrySet()) {
            HSSFRow row = signup.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getValue());

        }

    }
}
