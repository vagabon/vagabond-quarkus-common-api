package org.vagabond.common.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.vagabond.engine.auth.entity.BaseUserTokenEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_token")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserTokenEntity extends BaseUserTokenEntity {

    @Column(unique = true)
    public String token;

    public String ipAddress;

    public Boolean revoked;

    public LocalDateTime expiredDate;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public UserEntity user;

}
