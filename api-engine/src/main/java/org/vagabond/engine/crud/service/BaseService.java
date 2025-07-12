package org.vagabond.engine.crud.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.response.PageResponse;
import org.vagabond.engine.crud.utils.EntityUtils;
import org.vagabond.engine.crud.utils.query.IQueryUtils;
import org.vagabond.engine.crud.utils.query.QueryUtils;
import org.vagabond.engine.exeption.MetierException;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;

public abstract class BaseService<T extends BaseEntity> implements ICrudService<T>, IQueryUtils {

    @WithTransaction
    public T persist(T entity) {
        return getRepository().entityManager.merge(entity);
    }

    @WithTransaction
    public Uni<T> findById(Long id) {
        return getRepository().findById(id);
    }

    @WithTransaction
    public PageResponse<T> findByPage(Integer page, Integer max, String sort) {
        return queryPage("where active = ?1", page, max, sort, true);
    }

    public PageResponse<T> queryPage(String sql, Integer page, Integer max, String sort, Object... values) {
        if (sort != null && !sort.isEmpty()) {
            sql += " order by " + sort;
        }
        var activeQuery = getRepository().find(sql, values);
        if (page == null) {
            page = 0;
        }
        if (max == null || max > 500) {
            max = 500;
        }
        activeQuery.page(Page.ofSize(max));
        var numberOfPages = activeQuery.pageCount();
        var count = activeQuery.count();
        return new PageResponse<T>(page, numberOfPages, count, max, activeQuery.page(Page.of(page, max)).list());
    }

    @WithTransaction
    public T create(T entity) {
        if (entity.id != null) {
            throw new MetierException("Id is not null");
        }
        doBeforeCreate(entity);
        return getRepository().entityManager.merge(entity);
    }

    public void doBeforeCreate(T entity) {
    }

    @WithTransaction
    public T update(T entity) {
        if (entity.id == null) {
            throw new MetierException("Id is null");
        }
        var entityNew = getRepository().findById(entity.id).await().indefinitely();
        doBeforeMerge(entity, entityNew);
        EntityUtils.setEntity(entityNew, entity, true);
        doAfterMerge(entityNew);
        return getRepository().entityManager.merge(entityNew);
    }

    public void doBeforeMerge(T entity, T entityNew) {
    }

    public void doAfterMerge(T entity) {
    }

    @WithTransaction
    public Uni<Void> delete(Long id) {
        var entity = getRepository().findById(id).await().indefinitely();
        return getRepository().delete(entity);
    }

    @WithTransaction
    public PageResponse<T> constructQuery(Integer first, Integer max, String champs, Object... tabValues) {
        var query = getQuery(champs, tabValues);
        query.page(Page.ofSize(max));
        return new PageResponse<T>(first, query.pageCount(), query.count(), max, query.page(Page.of(first, max)).list());
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

    @WithTransaction
    public Uni<Long> countBy(String sql, Object... values) {
        return getRepository().countBy(sql, values);
    }

    @WithTransaction
    public Uni<List<T>> findBy(String sql, Object... values) {
        return getRepository().findBy(sql, values);
    }
}
