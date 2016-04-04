package com.ajdconsulting.pra.clubmanager.domain;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ScheduleDate.
 */
@Entity
@Table(name = "schedule_date")
public class ScheduleDate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @ManyToMany
    @JoinTable(name = "schedule_date_event_type",
               joinColumns = @JoinColumn(name="schedule_dates_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="event_types_id", referencedColumnName="ID"))
    private Set<EventType> eventTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(Set<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleDate scheduleDate = (ScheduleDate) o;
        if(scheduleDate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scheduleDate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleDate{" +
            "id=" + id +
            ", date='" + date + "'" +
            '}';
    }
}
