package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller to handle the results of webhooks that are passed to the application by external sources
 * which handle functions for us such as payment and form processing.
 *
 * @author adelimon
 * @since clubmanager 2017 release 2
 */
@Controller
@EnableWebSecurity
public class WebHookController {

    private final Logger log = LoggerFactory.getLogger(WebHookController.class);

    @Inject
    private MemberRepository memberRepository;

    /**
     * This is a custom handler to handle the webhook from Wufoo for the rules acknowledgement forms so that we don't
     * have to do them manually.  Because who wants to do that?
     *
     * The Wufoo form is here:
     * https://palmyraracing.wufoo.com/api/code/2/
     *
     * This spec is based on the form fields they generate there.  It is admittedly a bit fragile, but also pretty
     * simple so we can change it as needed.  What it lacks in complexity it makes up for in ease of use.
     *
     * @param request incoming POST from Wufoo
     * @param response simple response object to send back (just say OK always).
     */
    @RequestMapping(path = "/rulesAck", method = RequestMethod.POST)
    public void processRulesAcknowledgement(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("Field3");
        String email = request.getParameter("Field4");
        String agreement = request.getParameter("Field6");

        boolean emailValid = StringUtils.isNotEmpty(email);
        boolean agrees = (StringUtils.isNotEmpty(agreement) && "i agree".equals(agreement.toLowerCase()));

        if (emailValid && agrees) {
            Member member = memberRepository.findByEmail(email);
            member.setCurrentYearRenewed(true);
            memberRepository.save(member);
            log.info("Logging rules acknowledgement for " + member.getName() + " with email " + email);
        } else {
            log.info("No member found with email " + email);
        }

    }
}
