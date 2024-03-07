package org.vagabond.engine.crud.resource;

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
import org.vagabond.engine.auth.entity.BaseProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.crud.dto.PageResponse;
import org.vagabond.engine.crud.entity.BaseCrudEntity;
import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.utils.SecurityUtils;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public abstract class BaseCrudResource<T extends BaseEntity> extends BaseSecurityResource<T> {

    @POST
    @Path("/")
    public Response create(@Context SecurityContext contexte, @RequestBody T entity) {
        var userConnected = hasRole(contexte, roleModify);
        doBeforeCreate(userConnected, entity);
        var entityCreate = service.create(entity);
        doAfterCreate(userConnected, entityCreate);
        return responseOk(doAfterFindById(userConnected, entity));
    }

    public void doBeforeCreate(Object userConnected, T entity) {

    }

    public void doAfterCreate(Object userConnected, T entity) {
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

    public void doBeforeUpdate(Object userConnected, T entity) {

    }

    public void doAfterUpdate(Object userConnected, T entity) {
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
        }
        var entity = service.persist(entityBefore);
        doAfterDelete(userConnected, entity);
        return responseOk(Map.of("id", id));
    }

    public void doBeforeDelete(Object userConnected, T entityBefore) {
    }

    public void doAfterDelete(Object userConnected, T entity) {
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
        var userConnected = hasRole(contexte, roleRead);
        if (first == null) {
            first = 0;
        }
        if (max == null) {
            max = 100;
        }
        var response = service.constructQuery(first, max, fields, (Object[]) values.split(","));
        return responseOk(doAfterFindBy(userConnected, response));
    }

    protected <U extends BaseUserEntity<P>, P extends BaseProfileEntity> PageResponse doAfterFindBy(U userConnected,
            PageResponse response) {
        if (SecurityUtils.hasRole(userConnected, ADMIN)) {
            return response;
        }
        return toPage(response);
    }

    public abstract PageResponse toPage(PageResponse response);
}
