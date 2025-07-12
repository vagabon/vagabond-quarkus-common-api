package org.vagabond.engine.crud.entity;

import java.util.Objects;

import jakarta.persistence.MappedSuperclass;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity implements IEntity {

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        return id.equals(((BaseEntity) o).id);
    }
}
