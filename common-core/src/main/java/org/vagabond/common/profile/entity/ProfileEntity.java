package org.vagabond.common.profile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.vagabond.engine.auth.entity.BaseProfileEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "profile", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProfileEntity extends BaseProfileEntity {

}
