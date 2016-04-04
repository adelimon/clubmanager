package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;
    
    @NotNull
    @Min(value = 0)
    @Column(name = "point_value", nullable = false)
    private Float pointValue;
    
    @Column(name = "cash_value")
    private Float cashValue;
    
    @Column(name = "job_day")
    private String jobDay;
    
    @NotNull
    @Min(value = 0)
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
    
    @Column(name = "reserved")
    private Boolean reserved;
    
    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getJobDay() {
        return jobDay;
    }
    
    public void setJobDay(String jobDay) {
        this.jobDay = jobDay;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getReserved() {
        return reserved;
    }
    
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        if(job.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", pointValue='" + pointValue + "'" +
            ", cashValue='" + cashValue + "'" +
            ", jobDay='" + jobDay + "'" +
            ", sortOrder='" + sortOrder + "'" +
            ", reserved='" + reserved + "'" +
            '}';
    }
}
