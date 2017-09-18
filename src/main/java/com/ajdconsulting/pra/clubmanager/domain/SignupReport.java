package com.ajdconsulting.pra.clubmanager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SignupReport.
 */
@Entity
@Table(name = "signup_report")
public class SignupReport implements Serializable {

    @Id
    private Long id;

    @Column(name="job_id")
    private Long jobId;

    @Column(name="schedule_date_id")
    private Long scheduleDateId;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "point_value")
    private Float pointValue;

    @Column(name = "cash_value")
    private Float cashValue;

    @Column(name = "reserved")
    private Boolean reserved;

    @Column(name = "meal_ticket")
    private Boolean mealTicket;

    @Column(name = "job_day")
    private String jobDay;

    @Column(name = "leader")
    private String leader;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "signup_id")
    private Long signupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPointValue() {
        return pointValue;
    }

    public void setPointValue(Float pointValue) {
        this.pointValue = pointValue;
    }

    public Float getCashValue() {
        return cashValue;
    }

    public void setCashValue(Float cashValue) {
        this.cashValue = cashValue;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Boolean getMealTicket() {
        return mealTicket;
    }

    public void setMealTicket(Boolean mealTicket) {
        this.mealTicket = mealTicket;
    }

    public String getJobDay() {
        return jobDay;
    }

    public void setJobDay(String jobDay) {
        this.jobDay = jobDay;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignupReport signupReport = (SignupReport) o;
        if(signupReport.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, signupReport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SignupReport{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", title='" + title + "'" +
            ", pointValue='" + pointValue + "'" +
            ", cashValue='" + cashValue + "'" +
            ", reserved='" + reserved + "'" +
            ", jobDay='" + jobDay + "'" +
            ", leader='" + leader + "'" +
            ", date='" + date + "'" +
            '}';
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getScheduleDateId() {
        return scheduleDateId;
    }

    public void setScheduleDateId(Long scheduleDateId) {
        this.scheduleDateId = scheduleDateId;
    }

    public Long getSignupId() {
        return signupId;
    }

    public void setSignupId(Long signupId) {
        this.signupId = signupId;
    }
}
