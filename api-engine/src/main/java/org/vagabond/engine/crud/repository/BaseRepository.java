package org.vagabond.engine.crud.repository;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.exeption.MetierException;

import io.smallrye.mutiny.Uni;

public abstract class BaseRepository<T extends BaseEntity> implements IRepository<T> {

    public static final String ACTIVE = "active";
    public static final String WHERE = "where ";
    public static final String EQUAL_PARAM = " = ?1";
    public static final String ENTITY_NOT_FOUND_EXCEPTION = "ENTITY_NOT_FOUND";

    @Inject
    public EntityManager entityManager;

    public Uni<T> findByOneField(String field, String value) {
        return find(WHERE + field + EQUAL_PARAM, value).firstResult();
    }

    public Uni<T> findByOrThrow(String field, String value) {
        var entity = findByOneField(field, value);
        if (entity == null) {
            throw new MetierException(ENTITY_NOT_FOUND_EXCEPTION);
        }
        return entity;
    }

    public Uni<Long> countBy(String sql, Object... values) {
        return find(sql, values).count();
    }

    public Uni<List<T>> findBy(String sql, Object... values) {
        return find(sql, values).list();
    }

    public Uni<Boolean> existBy(String field, String value) {
        return count(WHERE + field + EQUAL_PARAM, value).map(count -> count > 0);
    }

    public Uni<Long> deleteBy(String sql, Object... values) {
        return delete(sql, values);
    }
}