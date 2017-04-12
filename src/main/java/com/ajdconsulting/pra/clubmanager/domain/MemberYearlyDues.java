package com.ajdconsulting.pra.clubmanager.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by adelimon on 4/12/2017.
 */
@Entity
@Table(name = "member_yearly_dues")
public class MemberYearlyDues {

    @Id
    private Long id;

    @NotNull
    @Column(name = "points")
    private Float points;

    @NotNull
    @Column(name = "amount_due")
    private Float amountDue;

    @NotNull
    @Column(name = "year")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Float amountDue) {
        this.amountDue = amountDue;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberYearlyDues that = (MemberYearlyDues) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
