package com.ajdconsulting.pra.clubmanager.integrations.mailchimp;

import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.util.Md5Converter;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
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

/**
 * Update an external mailing list for a given member record.
 *
 * @author adelimon
 * @since 3/24/17
 */
public class MailingList {

    private static final Logger log = LoggerFactory.getLogger(MailingList.class);

    public static void addMember(Member member) throws IOException, JSONException {
        MemberListClient listClient = new MemberListClient();
        if (listClient.isOnList(member)) {
            log.debug(member.getEmail() + " was found on mailchimp so we are done");
        } else {
            log.debug(member.getEmail() + " was not found, so going to create it on mailchimp...");
            boolean added = listClient.addToList(member);
            if (!added) {
                log.error("Failed to add member " + member.getName() + " email " + member.getEmail() +
                    " to external mailing list.  Please add manually.");
            }
        }

    }

    public static void updateMember(Member member) {

    }

    public static void deleteMember(Member member) {

    }

}
