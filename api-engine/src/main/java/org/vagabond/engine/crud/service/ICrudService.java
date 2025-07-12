package org.vagabond.engine.crud.service;

import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.response.PageResponse;

import io.smallrye.mutiny.Uni;

public interface ICrudService<T extends BaseEntity> extends IService {

    T persist(T entity);

    Uni<T> findById(Long id);

    PageResponse<T> findByPage(Integer page, Integer max, String sort);

    T create(T entity);

    T update(T entity);

    Uni<Void> delete(Long id);

    PageResponse<T> constructQuery(Integer first, Integer max, String champs, Object... tabValues);

    BaseRepository<T> getRepository();
}
