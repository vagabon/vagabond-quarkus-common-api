package org.vagabond.engine.crud.service;

import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.response.PageResponse;

public interface ICrudService<T extends BaseEntity> extends IService {

    T persist(T entity);

    T findById(Long id);

    PageResponse findByPage(Integer page, Integer max, String sort);

    T create(T entity);

    T update(T entity);

    T delete(Long id);

    PageResponse constructQuery(Integer first, Integer max, String champs, Object... tabValues);

    BaseRepository<T> getRepository();
}
