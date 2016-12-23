package com.ajdconsulting.pra.clubmanager.domain;

public class MemberDues {

    public static final float STANDARD_AMOUNT = 500;

    private String firstName;

    private String lastName;

    private String memberType;

    private float points;

    private float amountDue;

    public MemberDues(Member member) {
        this.setFirstName(member.getFirstName());
        this.setLastName(member.getLastName());
        this.setMemberType(member.getStatus().getType());
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
}
