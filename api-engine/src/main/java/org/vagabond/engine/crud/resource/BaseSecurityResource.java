package org.vagabond.engine.crud.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.vagabond.engine.auth.BaseAuthResource;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.auth.service.BaseAuthService;
import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.response.BaseResponse;
import org.vagabond.engine.crud.service.ICrudService;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.gateway.CurrentUser;
import org.vagabond.engine.gateway.GatewayUser;
import org.vagabond.engine.mapper.MapperUtils;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public abstract class BaseSecurityResource<T extends BaseEntity, U extends BaseUserEntity<?>>
    implements BaseResource {

    @Inject
    protected BaseAuthService<U, ?> authService;

    protected ICrudService<T> service;

    protected Class<? extends BaseResponse> responseClass;

    @Inject
    GatewayUser gatewayUser;

    @GET()
    @Path("/{id}")
    public Response findById(Long id) {
        var userConnected = getUserConnected();
        var entity = service.findById(id);
        return responseOk(doAfterFindById(userConnected, entity));
    }

    protected Object doAfterFindById(CurrentUser userConnected, T response) {
        if (userConnected != null && userConnected.hasRole(ADMIN)) {
            return response;
        }
        return toDto(response);
    }

    public Object toDto(T entity) {
        return responseClass != null ? MapperUtils.toDto(entity, responseClass) : entity;
    }

    protected CurrentUser getUserConnected() {
        var user = gatewayUser.get();
        if (user == null) {
            throw new MetierException(BaseAuthResource.REFRESH_TOKEN_ERROR);
        }
        return user;
    }

    protected void verifyUserConnected(CurrentUser user, Long id) {
        if (!user.id().equals(id) && !user.hasRole(ADMIN)) {
            throw new MetierException("ERRORS.NOT_ALLOWED");
        }
    }
}
