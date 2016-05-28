package com.ajdconsulting.pra.clubmanager.domain;

import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SignupReport.
 */
@Entity
@Table(name = "signup_report")
public class SignupReport implements Serializable {

    @Id
    private Long id;

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

    @Column(name = "job_day")
    private String jobDay;

    @Column(name = "leader")
    private String leader;

    @Column(name = "date")
    private LocalDate date;

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
}
