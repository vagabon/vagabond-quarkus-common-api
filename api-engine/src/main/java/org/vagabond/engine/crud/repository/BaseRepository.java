package org.vagabond.engine.crud.repository;

import java.util.List;

import org.vagabond.engine.crud.entity.BaseEntity;

public abstract class BaseRepository<T extends BaseEntity> implements IBaseRepository<T> {

    public static final String ACTIVE = "active";
    public static final String WHERE = "where ";

    public Long countBy(String sql, Object... values) {
        return find(sql, values).count();
    }

    public List<T> findBy(String sql, Object... values) {
        return find(sql, values).list();
    }
}