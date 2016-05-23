package com.ajdconsulting.pra.clubmanager.data.export.excel;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ExcelHttpOutputStream {

    private static final String XSLX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static OutputStream getOutputStream(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType(XSLX_CONTENT_TYPE);
        response.addHeader("content-disposition", "filename=" + fileName);
        return response.getOutputStream();
    }
}
