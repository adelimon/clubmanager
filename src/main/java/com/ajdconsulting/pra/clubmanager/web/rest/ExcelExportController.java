package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.Signup;
import com.ajdconsulting.pra.clubmanager.repository.SignupRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    public void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Fetch your data
        // 2. Create your excel
        // 3. write excel file to your response.

    }
}
