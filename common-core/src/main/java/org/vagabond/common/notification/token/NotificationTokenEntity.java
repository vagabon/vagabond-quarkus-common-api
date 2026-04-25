package org.vagabond.common.notification.token;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.vagabond.common.user.entity.UserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notification_token", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class NotificationTokenEntity extends BaseCrudEntity {

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;

    public String token;
}
