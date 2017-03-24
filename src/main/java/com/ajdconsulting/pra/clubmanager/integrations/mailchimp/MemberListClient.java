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
    private static final String apiKey = "-us15";

    private static final Logger log = LoggerFactory.getLogger(MemberListClient.class);
    private HttpClient client;

    public MemberListClient() {
        this.client = HttpClientBuilder.create().build();
    }

    public boolean isOnList(Member member) throws IOException {
        HttpGet request = new HttpGet(memberListUrl+"/"+ Md5Converter.toMd5(member.getEmail()));
        request.addHeader("Authorization: Basic", apiKey);
        /*
        HttpHost proxy = new HttpHost("edsbcs003.paychex.com", 80);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        request.setConfig(config);
        */

        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        return (statusCode == HttpStatus.SC_OK);
    }

    public boolean addToList(Member member) throws JSONException, IOException {
        MemberSubscription subscription = new MemberSubscription(member, MemberSubscription.STATUS_ADD);
        log.debug("Sending json object " + subscription.toString() + " to MailChimp API");

        HttpPost httpPost = new HttpPost(memberListUrl);
        httpPost.addHeader("Authorization: Basic", apiKey);
        httpPost.setEntity(new StringEntity(subscription.toString()));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        /*
        HttpHost proxy = new HttpHost("edsbcs003..com", 80);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpPost.setConfig(config);
        */

        HttpResponse httpResponse = client.execute(httpPost);
        int code = httpResponse.getStatusLine().getStatusCode();
        log.debug("Tried to add member " + member.getName() + " - " + member.getEmail() + " got code " + code );
        return (code == HttpStatus.SC_OK);
    }
}
