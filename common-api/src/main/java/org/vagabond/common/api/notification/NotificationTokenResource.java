package org.vagabond.common.api.notification;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.vagabond.common.notification.token.NotificationTokenEntity;
import org.vagabond.common.notification.token.NotificationTokenService;
import org.vagabond.common.notification.token.payload.NotificationTokenRequest;
import org.vagabond.common.notification.token.payload.NotificationTokenResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseSecurityResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/notification/token")
@RunOnVirtualThread
public class NotificationTokenResource extends BaseSecurityResource<NotificationTokenEntity, UserEntity> {

    @Inject
    NotificationTokenService notificationService;

    @PostConstruct
    public void postConstruct() {
        roleModify = ADMIN;
        roleRead = ADMIN;
        service = notificationService;
        responseClass = NotificationTokenResponse.class;
    }

    @PUT
    @Path("/user")
    public Response user(@Context SecurityContext contexte, @RequestBody NotificationTokenRequest request) {
        UserEntity userConnected = hasRole(contexte, "USER");
        verifyUserConnected(userConnected, request.userId());
        var mergeToken = notificationService.mergeToken(userConnected, request.token());
        return responseOk(doAfterFindById(userConnected, mergeToken));
    }
}