package org.vagabond.engine.auth.entity;

import org.vagabond.engine.crud.entity.BaseCrudEntity;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseProfileEntity extends BaseCrudEntity {

    public String name;

    public String roles;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
