package com.ajdconsulting.pra.clubmanager.domain;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MemberNote.
 */
@Entity
@Table(name = "member_note")
public class MemberNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "notes", nullable = false)
    private String notes;
    
    @Column(name = "date")
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
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
        MemberNote memberNote = (MemberNote) o;
        if(memberNote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, memberNote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemberNote{" +
            "id=" + id +
            ", notes='" + notes + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
