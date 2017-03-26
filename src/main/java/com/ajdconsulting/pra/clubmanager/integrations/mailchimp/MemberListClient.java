package com.ajdconsulting.pra.clubmanager.integrations.mailchimp;

import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.util.Md5Converter;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 */
public class MemberListClient {

    private static final String memberListUrl =  "http://us15.api.mailchimp.com/3.0/lists/cb965a0841/members";

    private static final Logger log = LoggerFactory.getLogger(MemberListClient.class);
    private HttpClient client;
    private String apiKey;

    public MemberListClient(String apiKey) {
        this.client = HttpClientBuilder.create().build();
        this.apiKey = apiKey;
    }

    public boolean isOnList(Member member) throws IOException {
        HttpGet request = new HttpGet(memberListUrl+"/"+ Md5Converter.toMd5(member.getEmail()));
        request.addHeader("Authorization: Basic", apiKey);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        return (statusCode == HttpStatus.SC_OK);
    }

    public boolean addToList(Member member) throws JSONException, IOException {
        MemberSubscription subscription = new MemberSubscription(member, MemberSubscription.STATUS_ADD);
        return postChange(member, subscription);
    }

    public boolean removeFromList(Member member) throws JSONException, IOException {
        MemberSubscription subscription = new MemberSubscription(member, MemberSubscription.STATUS_REMOVE);
        return postChange(member, subscription);
    }

    private boolean postChange(Member member, MemberSubscription subscription) throws IOException {
        log.debug("Sending json object " + subscription.toString() + " to MailChimp API");
        HttpPost httpPost = new HttpPost(memberListUrl);
        httpPost.addHeader("Authorization: Basic", apiKey);
        httpPost.setEntity(new StringEntity(subscription.toString()));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse httpResponse = client.execute(httpPost);
        int code = httpResponse.getStatusLine().getStatusCode();
        log.debug("Tried to change status of " + member.getName() + " - " + member.getEmail() + " got code " + code );
        return (code == HttpStatus.SC_OK);
    }
}
