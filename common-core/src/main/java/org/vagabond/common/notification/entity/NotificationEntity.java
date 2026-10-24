package org.vagabond.common.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notification", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class NotificationEntity extends BaseCrudEntity {

    public Long userId;

    public String title;

    @Column(columnDefinition = "TEXT")
    public String message;

    public String superType;
    public String category;
    public String type;

    public String url;

    public Long entityId;

    public String users;

    public Boolean read;
}
