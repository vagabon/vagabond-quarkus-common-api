package org.vagabond.engine.crud.resource;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.auth.service.BaseAuthService;
import org.vagabond.engine.crud.entity.BaseEntity;
import org.vagabond.engine.crud.response.BaseResponse;
import org.vagabond.engine.crud.service.ICrudService;
import org.vagabond.engine.crud.utils.SecurityUtils;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.mapper.MapperUtils;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
public abstract class BaseSecurityResource<T extends BaseEntity, U extends BaseUserEntity<?>> implements BaseResource {

    public static final String ADMIN = "ADMIN";

    @Inject
    protected BaseAuthService<U, ?> authService;

    protected ICrudService<T> service;

    protected Class<? extends BaseResponse> responseClass;

    @Inject
    JsonWebToken jwt;

    @Inject
    EntityManager entityManager;

    @GET()
    @Path("/{id}")
    public Response findById(Long id) {
        var userConnected = getUserConnected();
        var entity = service.findById(id);
        return responseOk(doAfterFindById(userConnected, entity));
    }

    protected Object doAfterFindById(U userConnected, T response) {
        if (SecurityUtils.hasRole(userConnected, ADMIN)) {
            return response;
        }
        return toDto(response);
    }

    public Object toDto(T entity) {
        return responseClass != null ? MapperUtils.toDto(entity, responseClass) : entity;
    }

    protected U getUserConnected() {
        return authService.findByUsername(jwt.getName());
    }

    protected void verifyUserConnected(U user, Long id) {
        if (!user.id.equals(id) && user.getProfiles().stream().filter(profile -> profile.roles.contains((ADMIN))).toList().isEmpty()) {
            throw new MetierException("ERRORS.NOT_ALLOWED");
        }
    }
}
