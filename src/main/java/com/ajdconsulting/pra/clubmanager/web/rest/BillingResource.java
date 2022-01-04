package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.domain.Integration;
import com.ajdconsulting.pra.clubmanager.repository.IntegrationRepository;
import com.codahale.metrics.annotation.Timed;
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

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

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
        
        Integration billingPublishLocation = integrationRepository.findPlatformById("awsBillingTopic");
        
        SnsClient snsClient = SnsClient.builder()
            .region(Region.US_EAST_1)
            .build();

        PublishRequest request = PublishRequest.builder()
            .message(memberId.toString())
            .topicArn(billingPublishLocation.getApikey())
            .build();

        PublishResponse result = snsClient.publish(request);
        log.info(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());
        return new ResponseEntity<JSONObject>(new JSONObject(memberId), HttpStatus.OK);
    }

}
