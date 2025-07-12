package org.vagabond.engine.crud.repository;

import java.util.List;

import org.vagabond.engine.crud.entity.BaseEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

public interface IRepository<T extends BaseEntity> extends PanacheRepository<T> {

    Uni<T> findByOneField(String field, String value);

    Uni<List<T>> findBy(String sql, Object... values);

    Uni<Boolean> existBy(String field, String value);

    Uni<Long> deleteBy(String sql, Object... values);

}
