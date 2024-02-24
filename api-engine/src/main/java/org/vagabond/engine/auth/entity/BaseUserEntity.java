package org.vagabond.engine.auth.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

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

}
