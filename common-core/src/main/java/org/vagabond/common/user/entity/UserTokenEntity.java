package org.vagabond.common.user.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.vagabond.engine.auth.entity.BaseUserTokenEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_token")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class UserTokenEntity extends BaseUserTokenEntity {

    @Column(unique = true)
    public String token;

    public String ipAddress;

    public Boolean revoked;

    public Instant expiredDate;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;

}
