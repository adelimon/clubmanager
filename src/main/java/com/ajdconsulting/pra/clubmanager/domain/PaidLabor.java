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
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "member_linked_to_id")
    private Member memberLinkedTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
            ", name='" + name + "'" +
            '}';
    }
}
