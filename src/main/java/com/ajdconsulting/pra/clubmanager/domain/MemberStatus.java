package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MemberStatus.
 */
@Entity
@Table(name = "member_status")
public class MemberStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1961)
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "member_types_id")
    private MemberTypes memberTypes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MemberTypes getMemberTypes() {
        return memberTypes;
    }

    public void setMemberTypes(MemberTypes memberTypes) {
        this.memberTypes = memberTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberStatus memberStatus = (MemberStatus) o;
        if(memberStatus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, memberStatus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemberStatus{" +
            "id=" + id +
            ", year='" + year + "'" +
            '}';
    }
}
