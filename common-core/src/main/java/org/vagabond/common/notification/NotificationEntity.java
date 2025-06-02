package org.vagabond.common.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NotificationEntity extends BaseCrudEntity {

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;

    public String title;

    @Column(columnDefinition = "TEXT")
    public String message;

    @Column(name = "super_type")
    public String superType;
    public String category;
    public String type;

    public String url;

    @Column(name = "entity_id")
    public Long entityId;

    public String users;

    public Boolean read;
}
