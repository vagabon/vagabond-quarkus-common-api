package org.vagabond.engine.crud.resource;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.vagabond.engine.auth.decorator.AuthRole;
import org.vagabond.engine.crud.entity.BaseEntity;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public abstract class BaseCrudUserResource<T extends BaseEntity>
    extends BaseCrudResource<T> {

    @Override
    @AuthRole("USER")
    public Response create(@RequestBody T entity) {
        return super.create(entity);
    }

    @Override
    @AuthRole("USER")
    public Response update(@RequestBody T entity) {
        return super.update(entity);
    }

    @Override
    @AuthRole("USER")
    public Response desactivate(@QueryParam("id") Long id) {
        return super.desactivate(id);
    }
}
