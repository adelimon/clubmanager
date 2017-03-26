package com.ajdconsulting.pra.clubmanager.integrations.mailchimp;

import com.ajdconsulting.pra.clubmanager.domain.Member;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A wrapper object that represents the member email as an address on mailchimp
 *
 * @author adelimon
 */
public class MemberSubscription {

    public static final String STATUS_ADD = "subscribed";
    public static final String STATUS_REMOVE = "unsubscribed";

    private JSONObject jsonSubscription;

    public MemberSubscription(Member member, String status) throws JSONException {
        jsonSubscription = new JSONObject();
        jsonSubscription.put("email_address", member.getEmail());
        jsonSubscription.put("status", status);
        JSONObject firstName = new JSONObject();
        firstName.put("FNAME", member.getFirstName());
        firstName.put("LNAME", member.getLastName());
        jsonSubscription.put("merge_fields", firstName);
        jsonSubscription.put("email_type", "html");
    }

    public String toJson() {
        return jsonSubscription.toString();
    }

    public String toString() {
        return this.toJson();
    }
}
