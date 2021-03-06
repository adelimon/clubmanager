package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EventType.
 */
@Entity
@Table(name = "event_type")
public class EventType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventType eventType = (EventType) o;
        if(eventType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, eventType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventType{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }

    public int getStartHour() {
        int startTime = 0;
        switch (type) {
            case "Meeting":
                startTime = 19;
                break;
            case "Work Day":
                startTime = 9;
                break;
            default:
                startTime = 8;
                break;
        }
        return startTime;
    }

    public int getEndHour() {
        int startTime = 0;
        switch (type) {
            case "Meeting":
                startTime = 20;
                break;
            case "Work Day":
                startTime = 13;
                break;
            default:
                startTime = 18;
                break;
        }
        return startTime;
    }

    public int getStartMinute() {
        int startMinute = 0;
        switch (type) {
            case "Meeting":
                startMinute = 30;
                break;
            default:
                startMinute = 0;
                break;
        }
        return startMinute;
    }
}
