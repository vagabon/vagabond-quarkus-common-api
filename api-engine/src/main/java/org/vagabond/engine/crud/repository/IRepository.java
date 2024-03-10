package org.vagabond.engine.crud.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.vagabond.engine.crud.entity.BaseEntity;

public interface IRepository<T extends BaseEntity> extends PanacheRepository<T> {

    T findByOneField(String field, String value);

    List<T> findBy(String sql, Object... values);

    boolean existBy(String field, String value);

    long deleteBy(String sql, Object... values);

}
