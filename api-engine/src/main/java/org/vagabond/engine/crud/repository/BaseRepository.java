package org.vagabond.engine.crud.repository;

import java.util.List;

import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.exeption.MetierException;

public abstract class BaseRepository<T extends BaseEntity> implements IRepository<T> {

    public static final String ACTIVE = "active";
    public static final String WHERE = "where ";
    public static final String EQUAL_PARAM = " = ?1";
    public static final String ENTITY_NOT_FOUND_EXCEPTION = "ENTITY_NOT_FOUND";

    public T findBy(String field, String value) {
        return find(WHERE + field + EQUAL_PARAM, value).firstResult();
    }

    public T findByOrThrow(String field, String value) {
        var entity = findBy(field, value);
        if (entity == null) {
            throw new MetierException(ENTITY_NOT_FOUND_EXCEPTION);
        }
        return entity;
    }

    public Long countBy(String sql, Object... values) {
        return find(sql, values).count();
    }

    public List<T> findBy(String sql, Object... values) {
        return find(sql, values).list();
    }

    public boolean existBy(String field, String value) {
        return count(WHERE + field + EQUAL_PARAM, value) > 0;
    }

    public long deleteBy(String sql, Object... values) {
        return delete(sql, values);
    }
}