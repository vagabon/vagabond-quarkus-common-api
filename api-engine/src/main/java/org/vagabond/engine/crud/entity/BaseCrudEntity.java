package org.vagabond.engine.crud.entity;

import java.time.LocalDateTime;

import io.quarkus.logging.Log;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
@Table(schema = "public")
public abstract class BaseCrudEntity extends BaseEntity {

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime creationDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime deletedDate;

    public Boolean active;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        Log.debugf("persist %s", this.toString());
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
        this.updatedDate = LocalDateTime.now();
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
