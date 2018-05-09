package com.ajdconsulting.pra.clubmanager.renewals;

import com.ajdconsulting.pra.clubmanager.domain.MemberBill;
import com.ajdconsulting.pra.clubmanager.domain.MemberDues;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Formatter;

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

    public void setVariables(MemberBill bill) {
        contents = contents.replace("{MEMBER_NAME}", (bill.getMember().getFirstNameLastName()));
        contents = contents.replace("{STATUS}", bill.getMember().getStatus().getType());
        contents = contents.replace("{DUES}", formatCurrency(bill.getAmount()));
        contents = contents.replace("DUESPLUSFEE", bill.getAmountWithFee()+"");
        contents = contents.replace("EMAIL", bill.getMember().getEmail());
        contents = contents.replace( "{YEAR}", bill.getYear()+"");
    }


    public void setContents(String contents) {
        this.contents = contents;
    }

    private String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(amount);
    }

    public String toString() {
        return contents;
    }
}
