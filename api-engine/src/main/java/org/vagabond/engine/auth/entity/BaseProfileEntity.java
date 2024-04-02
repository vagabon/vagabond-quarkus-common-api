package org.vagabond.engine.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.vagabond.engine.crud.entity.BaseCrudEntity;

@MappedSuperclass
public abstract class BaseProfileEntity extends BaseCrudEntity {

    public String name;

    public String roles;

    @Column(name = "end_plan")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime endPlan;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
