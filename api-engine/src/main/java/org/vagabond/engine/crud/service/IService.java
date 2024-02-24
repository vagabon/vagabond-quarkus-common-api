package org.vagabond.engine.crud.service;

import org.vagabond.engine.crud.dto.PageResponse;
import org.vagabond.engine.crud.entity.BaseEntity;

public interface IService<T extends BaseEntity> {

    T persist(T entity);

    T findById(Long id);

    PageResponse findByPage(Integer page, Integer max, String sort);

    T create(T entity);

    T update(T entity);

    T delete(Long id);

    PageResponse constructQuery(Integer first, Integer max, String champs, Object... tabValues);
}
