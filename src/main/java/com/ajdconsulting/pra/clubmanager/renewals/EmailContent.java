package com.ajdconsulting.pra.clubmanager.renewals;

import com.ajdconsulting.pra.clubmanager.domain.MemberDues;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Email content wrapper class.  yo yo
 */
public class EmailContent {

    private String contents;

    public EmailContent(String emailName) throws IOException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        contents = new String(Files.readAllBytes(Paths.get(s + "/src/main/resources/mails/" + emailName + "Email.html")));
    }

    public void setVariables(MemberDues dues) {
        contents = contents.replace("{MEMBER_NAME}", (dues.getFirstName() + " " + dues.getLastName()));
        contents = contents.replace("{STATUS}", dues.getMemberType());
        contents = contents.replace("{DUES}", dues.getAmountDue()+"");
        contents = contents.replace("DUESPLUSFEE", dues.getAmountWithFee()+"");
        contents = contents.replace("EMAIL", dues.getEmail());
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String toString() {
        return contents;
    }
}
