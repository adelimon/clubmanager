package com.ajdconsulting.pra.clubmanager.scheduled;

import com.ajdconsulting.pra.clubmanager.util.Md5Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONString;
import org.json.JSONWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by adelimon on 3/18/2017.
 */
public class MailChimpClient {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String url = "https://us15.api.mailchimp.com/3.0/lists/cb965a0841/members";
        String email = "osome guys email address";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url+"/"+ Md5Converter.toMd5(email));
        request.addHeader("Authorization: Basic", "NOPE NOPE NOPE NOPE");

        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            System.out.println(email + " was found on mailchimp so we are done");

            System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result.toString());
            System.out.println(json);

        } else {
            System.out.println(email + " was not found, so going to create it on mailchimp...");

        }
    }
}
