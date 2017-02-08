package com.ajdconsulting.pra.clubmanager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @NotNull
    @Size(min = 5, max = 5)
    @Column(name = "zip", length = 5, nullable = false)
    private String zip;

    @Column(name = "occupation")
    private String occupation;

    @NotNull
    @Column(name = "phone", length = 12, nullable = true)
    private String phone;

    @Column(name = "view_online")
    private Boolean viewOnline;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "date_joined")
    private LocalDate dateJoined;

    @Column(name = "current_year_points")
    private Float currentYearPoints;

    @ManyToOne
    @JoinColumn(name = "status")
    private MemberTypes status;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "renewal_sent")
    private Boolean renewalSent=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return lastName + ", " + firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getViewOnline() {
        return viewOnline;
    }

    public void setViewOnline(Boolean viewOnline) {
        this.viewOnline = viewOnline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Float getCurrentYearPoints() {
        return currentYearPoints;
    }

    public void setCurrentYearPoints(Float currentYearPoints) {
        this.currentYearPoints = currentYearPoints;
    }

    public MemberTypes getStatus() {
        return status;
    }

    public void setStatus(MemberTypes status) {
        this.status = status;
    }

    public boolean isPaidLabor() {
        return ("Paid Labor".equals(this.status.getType()));
    }

    public boolean paysDues() {
        return !("Life member".equals(status.getType()) ||
            "Sponsorship".equals(status.getType()) ||
            "Distant Rider".equals(status.getType())
        );
    }

    public float getTotalDues(float totalPoints) {
        float totalDues;
        // life members and sponsored members pay no dues.
        if (this.paysDues()) {
            // Everyone else pays the total dues, minus the number of points, divided by standard amount.
            float standardAmount = MemberDues.STANDARD_AMOUNT;
            float earnedAmount = (totalPoints * MemberDues.PAID_PER_POINT);
            totalDues = (standardAmount - earnedAmount);
            // If the total goes negative, then their total due is zero.  We don't pay people for points
            // overages, we just say THANK YOU FOR YER SERVICE
            if (totalDues < 0) {
                totalDues = 0;
            }
            if ("Reserve".equals(status.getType())) {
                totalDues = 40.0f;
            }
        } else {
            totalDues = 0;
        }
        return totalDues;
    }

    public float getTotalPoints(List<EarnedPoints> memberPoints) {

        float totalPoints = 0;
        for (EarnedPoints entry : memberPoints) {
            totalPoints += entry.getPointValue();
        }
        return totalPoints;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        if(member.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", address='" + address + "'" +
            ", city='" + city + "'" +
            ", state='" + state + "'" +
            ", zip='" + zip + "'" +
            ", occupation='" + occupation + "'" +
            ", phone='" + phone + "'" +
            ", viewOnline='" + viewOnline + "'" +
            ", email='" + email + "'" +
            ", birthday='" + birthday + "'" +
            ", dateJoined='" + dateJoined + "'" +
            '}';
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isRenewalSent() {
        return renewalSent;
    }

    public void setRenewalSent(boolean renewalSent) {
        this.renewalSent = renewalSent;
    }
}
