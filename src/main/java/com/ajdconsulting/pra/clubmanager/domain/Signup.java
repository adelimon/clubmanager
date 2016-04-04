package com.ajdconsulting.pra.clubmanager.domain;


import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Signup.
 */
@Entity
@Table(name = "signup")
public class Signup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Member worker;

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

    public Member getWorker() {
        return worker;
    }

    public void setWorker(Member member) {
        this.worker = member;
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
        Signup signup = (Signup) o;
        if(signup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, signup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Signup{" +
            "id=" + id +
            '}';
    }

}
