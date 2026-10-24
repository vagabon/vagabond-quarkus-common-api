package org.vagabond.engine.crud.repository;

import java.util.List;

import org.vagabond.engine.crud.entity.BaseEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface IBaseRepository<T extends BaseEntity> extends PanacheRepository<T> {

    List<T> findBy(String sql, Object... values);

}
