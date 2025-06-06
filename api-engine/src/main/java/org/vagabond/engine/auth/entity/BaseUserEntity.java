package org.vagabond.engine.auth.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUserEntity<P extends BaseProfileEntity> extends BaseCrudEntity {

    @Column(unique = true)
    public String username;

    public String password;
    public String email;

    @Column(name = "date_derniere_connexion")
    public LocalDateTime lastConnexionDate;

    @Column(name = "connection_trials")
    public Integer connectionTrials;

    @Column(name = "date_derniere_tentative")
    public LocalDateTime lastFailedTrialDate;

    public abstract List<P> getProfiles();

    public abstract void setProfiles(List<P> newProfiles);

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
