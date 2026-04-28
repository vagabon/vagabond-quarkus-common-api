package org.vagabond.engine.crud.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import io.quarkus.logging.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseCrudEntity extends BaseEntity {

    @Column(name = "creation_date")
    public Instant creationDate;

    @Column(name = "updated_date")
    public Instant updatedDate;

    @Column(name = "deleted_date")
    public Instant deletedDate;

    public Boolean active;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        Log.debugf("persist %s", this.toString());
        if (this.creationDate == null) {
            this.creationDate = Instant.now();
        }
        this.updatedDate = Instant.now();
        if (this.active == null) {
            this.active = true;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
