package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PaidLabor.
 */
@Entity
@Table(name = "paid_labor")
public class PaidLabor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @ManyToOne
    @JoinColumn(name = "member_linked_to_id")
    private Member memberLinkedTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Member getMemberLinkedTo() {
        return memberLinkedTo;
    }

    public void setMemberLinkedTo(Member member) {
        this.memberLinkedTo = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaidLabor paidLabor = (PaidLabor) o;
        if(paidLabor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, paidLabor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaidLabor{" +
            "id=" + id +
            ", lastName='" + lastName + "'" +
            ", firstName='" + firstName + "'" +
            '}';
    }
}
