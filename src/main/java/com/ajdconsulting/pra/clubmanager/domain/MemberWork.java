package com.ajdconsulting.pra.clubmanager.domain;

import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MemberWork.
 */
@Entity
@Table(name = "member_work")
public class MemberWork implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "start", nullable = false)
    private ZonedDateTime start;
    
    @NotNull
    @Column(name = "end", nullable = false)
    private ZonedDateTime end;
    
    @NotNull
    @Size(min = 5)
    @Column(name = "description", nullable = false)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStart() {
        return start;
    }
    
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }
    
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberWork memberWork = (MemberWork) o;
        if(memberWork.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, memberWork.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemberWork{" +
            "id=" + id +
            ", start='" + start + "'" +
            ", end='" + end + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
