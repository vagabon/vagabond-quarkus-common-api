package org.vagabond.engine.crud.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.response.BaseResponse;
import org.vagabond.engine.crud.service.ICrudService;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.gateway.user.GatewayUser;
import org.vagabond.engine.mapper.MapperUtils;
import org.vagabond.engine.user.CurrentUser;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public abstract class BaseSecurityResource<T extends BaseEntity>
    implements BaseResource {

    public static final String USER_NOT_CONNECTED = "USER_NOT_CONNECTED";
    public static final String USER_NOT_ALLOWED = "USER_NOT_ALLOWED";

    protected ICrudService<T> service;

    protected Class<? extends BaseResponse> responseClass;

    @Inject
    protected GatewayUser gatewayUser;

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
        return gatewayUser.get();
    }

    protected void verifyUserConnected(CurrentUser user, Long id) {
        if (!user.id().equals(id) && !user.hasRole(ADMIN)) {
            throw new MetierException("ERRORS.NOT_ALLOWED");
        }
    }
}
