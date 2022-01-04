package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.Integration;
import com.ajdconsulting.pra.clubmanager.repository.IntegrationRepository;
import com.codahale.metrics.annotation.Timed;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Billing for PRA members.
 *
 * @author adelimon
 */
@RestController
@RequestMapping("/api")
public class BillingResource {

    private final Logger log = LoggerFactory.getLogger(BoardMemberResource.class);

    @Inject
    public IntegrationRepository integrationRepository;

    @RequestMapping(value = "/billing/send/{memberId}/{year}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JSONObject> generateBill(@PathVariable Long memberId, @PathVariable Integer year) throws IOException {
        
        Integration resendEndpoint = integrationRepository.findPlatformById("resendEndpoint");
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(resendEndpoint.getApikey() + memberId);
        HttpResponse httpResponse = client.execute(get);
        int code = httpResponse.getStatusLine().getStatusCode();

        return new ResponseEntity<JSONObject>(new JSONObject(code), HttpStatus.OK);
    }

}
