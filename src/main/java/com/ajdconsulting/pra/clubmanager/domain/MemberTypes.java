package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MemberTypes.
 */
@Entity
@Table(name = "member_types")
public class MemberTypes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Null
    @Column(name = "base_dues_amount", nullable = true)
    private Float baseDuesAmount;

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
        MemberTypes memberTypes = (MemberTypes) o;
        if(memberTypes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, memberTypes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MemberTypes{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }

    public Float getBaseDuesAmount() {
        return baseDuesAmount;
    }

    public void setBaseDuesAmount(Float baseDuesAmount) {
        this.baseDuesAmount = baseDuesAmount;
    }
}
