package org.vagabond.common.notification.token;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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

    public Long userId;

    public String token;
}
