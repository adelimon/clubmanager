package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PaidSignup.
 */
@Entity
@Table(name = "paid_signup")
public class PaidSignup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paid_labor_id")
    private PaidLabor paidLabor;

    @ManyToOne
    @JoinColumn(name = "schedule_date_id")
    private ScheduleDate scheduleDate;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaidLabor getPaidLabor() {
        return paidLabor;
    }

    public void setPaidLabor(PaidLabor paidLabor) {
        this.paidLabor = paidLabor;
    }

    public ScheduleDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(ScheduleDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaidSignup paidSignup = (PaidSignup) o;
        if(paidSignup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, paidSignup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaidSignup{" +
            "id=" + id +
            '}';
    }
}
