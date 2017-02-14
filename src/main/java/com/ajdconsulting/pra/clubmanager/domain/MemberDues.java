package com.ajdconsulting.pra.clubmanager.domain;

public class MemberDues {

    public static final float STANDARD_AMOUNT = 500;

    public static final float PAID_PER_POINT = 20;

    private String firstName;

    private String lastName;

    private String memberType;

    private float points;

    private float amountDue;
    private String email;
    private long memberId;
    private String paid;
    private String renewed;

    public MemberDues(Member member) {
        this.setFirstName(member.getFirstName());
        this.setLastName(member.getLastName());
        this.setMemberType(member.getStatus().getType());
        this.setEmail(member.getEmail());
        this.setMemberId(member.getId());
        if (member.getCurrentYearPaid()) {
            paid = "Yes";
        } else {
            paid = "No";
        }
        if (member.getCurrentYearRenewed()) {
            renewed = "Yes";
        } else {
            renewed = "No";
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(float amountDue) {
        this.amountDue = amountDue;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getRenewed() {
        return renewed;
    }

    public void setRenewed(String renewed) {
        this.renewed = renewed;
    }
}
