package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RidingAreaStatus.
 */
@Entity
@Table(name = "riding_area_status")
public class RidingAreaStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "area_name", nullable = false)
    private String areaName;
    
    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }
    
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RidingAreaStatus ridingAreaStatus = (RidingAreaStatus) o;
        if(ridingAreaStatus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ridingAreaStatus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RidingAreaStatus{" +
            "id=" + id +
            ", areaName='" + areaName + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
