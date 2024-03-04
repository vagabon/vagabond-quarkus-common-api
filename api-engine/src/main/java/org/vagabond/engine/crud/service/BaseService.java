package org.vagabond.engine.crud.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.apache.commons.lang3.BooleanUtils;
import org.vagabond.engine.crud.dto.PageResponse;
import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.query.IQueryUtils;
import org.vagabond.engine.crud.utils.EntityUtils;
import org.vagabond.engine.crud.utils.QueryUtils;
import org.vagabond.engine.exeption.MetierException;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

public abstract class BaseService<T extends BaseEntity> implements IService<T>, IQueryUtils {

    @Transactional
    public T persist(T entity) {
        getRepository().getEntityManager().merge(entity);
        return entity;
    }

    @Transactional
    public T findById(Long id) {
        return getRepository().findById(id);
    }

    @Transactional
    public PageResponse findByPage(Integer page, Integer max, String sort) {
        return queryPage("where active = ?1", page, max, sort, true);
    }

    public PageResponse queryPage(String sql, Integer page, Integer max, String sort, Object... values) {
        if (sort != null && !sort.isEmpty()) {
            sql += " order by " + sort;
        }
        PanacheQuery<T> activeQuery = getRepository().find(sql, values);
        if (page == null) {
            page = 0;
        }
        if (max == null || max > 500) {
            max = 500;
        }
        activeQuery.page(Page.ofSize(max));
        var numberOfPages = activeQuery.pageCount();
        var count = activeQuery.count();
        return new PageResponse(page, numberOfPages, count, max, activeQuery.page(Page.of(page, max)).list());
    }

    @Transactional
    public T create(T entity) {
        if (entity.id != null) {
            throw new MetierException("Id is not null");
        }
        doBeforeCreate(entity);
        getRepository().getEntityManager().merge(entity);
        return entity;
    }

    public void doBeforeCreate(T entity) {
    }

    @Transactional
    public T update(T entity) {
        if (entity.id == null) {
            throw new MetierException("Id is null");
        }
        var entityNew = getRepository().findById(entity.id);
        doBeforeMerge(entity, entityNew);
        EntityUtils.setEntity(entityNew, entity);
        doAfterMerge(entityNew);
        getRepository().getEntityManager().merge(entityNew);
        return entityNew;
    }

    public void doBeforeMerge(T entity, T entityNew) {
    }

    public void doAfterMerge(T entity) {
    }

    @Transactional
    public T delete(Long id) {
        var entity = getRepository().findById(id);
        getRepository().delete(entity);
        return entity;
    }

    @Transactional
    public PageResponse constructQuery(Integer first, Integer max, String champs, Object... tabValues) {
        var query = getQuery(champs, tabValues);
        query.page(Page.ofSize(max));
        return new PageResponse(first, query.pageCount(), query.count(), max, query.page(Page.of(first, max)).list());
    }

    private PanacheQuery<T> getQuery(String champs, Object... tabValues) {
        var endQuery = new StringBuilder();
        var queryDtos = initQueryDto(endQuery, champs, tabValues);
        var sqlQuery = getSqlQuery(queryDtos, false, endQuery);
        var values = new ArrayList<Object>();
        for (var i = 0; i < queryDtos.size(); i++) {
            var value = tabValues[i];
            if (BooleanUtils.isTrue(queryDtos.get(i).like)) {
                value = QueryUtils.getLike(tabValues[i].toString());
            } else if ("true".equals(value)) {
                value = Boolean.TRUE;
            } else if ("false".equals(value)) {
                value = Boolean.FALSE;
            }
            values.add(value);
        }
        return getRepository().find(sqlQuery, values.toArray());
    }

    @Transactional
    public Long countBy(String sql, Object... values) {
        return getRepository().countBy(sql, values);
    }

    @Transactional
    public List<T> findBy(String sql, Object... values) {
        return getRepository().findBy(sql, values);
    }

    public abstract BaseRepository<T> getRepository();
}
