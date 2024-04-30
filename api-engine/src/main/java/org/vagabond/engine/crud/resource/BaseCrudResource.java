package org.vagabond.engine.crud.resource;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.crud.dto.PageResponse;
import org.vagabond.engine.crud.entity.BaseCrudEntity;
import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.utils.QueryUtils;
import org.vagabond.engine.crud.utils.SecurityUtils;
import org.vagabond.engine.mapper.MapperUtils;

import io.quarkus.panache.common.Page;
import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public abstract class BaseCrudResource<T extends BaseEntity, U extends BaseUserEntity<?>> extends BaseSecurityResource<T, U> {

    @POST
    @Path("/")
    public Response create(@Context SecurityContext contexte, @RequestBody T entity) {
        var userConnected = hasRole(contexte, roleModify);
        doBeforeCreate(userConnected, entity);
        var entityCreate = service.create(entity);
        doAfterCreate(userConnected, entityCreate);
        return responseOk(doAfterFindById(userConnected, entityCreate));
    }

    public void doBeforeCreate(U userConnected, T entity) {
    }

    public void doAfterCreate(U userConnected, T entity) {
    }

    @PUT
    @Path("/")
    public Response update(@Context SecurityContext contexte, @RequestBody T entity) {
        var userConnected = hasRole(contexte, roleModify);
        doBeforeUpdate(userConnected, entity);
        var update = service.update(entity);
        doAfterUpdate(userConnected, update);
        return responseOk(doAfterFindById(userConnected, update));
    }

    public void doBeforeUpdate(U userConnected, T entity) {
    }

    public void doAfterUpdate(U userConnected, T entity) {
    }

    @DELETE
    @Path("/")
    public Response delete(@Context SecurityContext contexte, @QueryParam("id") Long id) {
        var userConnected = hasRole(contexte, roleModify);
        var entityBefore = service.findById(id);
        doBeforeDelete(userConnected, entityBefore);
        var entity = service.delete(id);
        doAfterDelete(userConnected, entity);
        return responseOk(Map.of("id", id));
    }

    @DELETE
    @Path("/desactivate")
    public Response desactivate(@Context SecurityContext contexte, @QueryParam("id") Long id) {
        var userConnected = hasRole(contexte, roleModify);
        var entityBefore = service.findById(id);
        doBeforeDelete(userConnected, entityBefore);
        if (entityBefore instanceof BaseCrudEntity crudEntity) {
            crudEntity.active = false;
            crudEntity.deletedDate = LocalDateTime.now();
        }
        var entity = service.persist(entityBefore);
        doAfterDelete(userConnected, entity);
        return responseOk(Map.of("id", id));
    }

    public void doBeforeDelete(U userConnected, T entityBefore) {
    }

    public void doAfterDelete(U userConnected, T entity) {
    }

    @GET
    @Path("/")
    public Response findAll(@Context SecurityContext contexte, @QueryParam("page") Integer page, @QueryParam("max") Integer max,
            @QueryParam("sort") String sort) {
        var userConnected = hasRole(contexte, roleRead);
        var response = service.findByPage(page, max, sort);
        return responseOk(doAfterFindBy(userConnected, response));
    }

    @GET
    @Path("/findBy")
    public Response findBy(@Context SecurityContext contexte, @QueryParam("fields") String fields, @QueryParam("values") String values,
            @QueryParam("first") Integer first, @QueryParam("max") Integer max) {
        var userConnected = hasRole(contexte, roleFindBy);
        if (first == null) {
            first = 0;
        }
        if (max == null) {
            max = 100;
        }
        var response = service.constructQuery(first, max, fields, (Object[]) values.split(","));
        return responseOk(doAfterFindBy(userConnected, response));
    }

    protected PageResponse doAfterFindBy(U userConnected, PageResponse response) {
        if (SecurityUtils.hasRole(userConnected, ADMIN)) {
            return response;
        }
        return toPage(response);
    }

    public PageResponse toPage(PageResponse response) {
        return responseClass != null ? MapperUtils.toPage(response, responseClass) : response;
    }

    public Response doSearch(U userEntity, StringBuilder hqlQuery, String fields, String values, Integer first, Integer max) {
        var valuesSplits = fields.split(">>");

        hqlQuery.append("AND e.active = true ");
        hqlQuery.append(" ORDER BY e.").append(valuesSplits[1].replace("Desc", " desc"));

        var query = service.getRepository().find(hqlQuery.toString(), QueryUtils.getLike(values));
        query.page(Page.ofSize(max));
        PageResponse response = new PageResponse(first, query.pageCount(), query.count(), max, query.page(Page.of(first, max)).list());
        return responseOk(doAfterFindBy(userEntity, response));

    }
}
