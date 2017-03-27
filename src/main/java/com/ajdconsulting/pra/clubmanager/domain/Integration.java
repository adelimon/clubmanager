package com.ajdconsulting.pra.clubmanager.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Integration.
 */
@Entity
@Table(name = "integration")
public class Integration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "platform", nullable = false)
    private String platform;
    
    @NotNull
    @Column(name = "apikey", nullable = false)
    private String apikey;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getApikey() {
        return apikey;
    }
    
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Integration integration = (Integration) o;
        if(integration.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, integration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Integration{" +
            "id=" + id +
            ", platform='" + platform + "'" +
            ", apikey='" + apikey + "'" +
            '}';
    }
}
