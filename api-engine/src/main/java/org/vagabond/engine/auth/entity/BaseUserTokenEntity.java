package org.vagabond.engine.auth.entity;

import jakarta.persistence.MappedSuperclass;

import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseUserTokenEntity extends BaseCrudEntity {

}
