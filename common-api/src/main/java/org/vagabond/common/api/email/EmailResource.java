package org.vagabond.common.api.email;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.vagabond.common.email.EmailService;
import org.vagabond.common.email.payload.EmailRequest;
import org.vagabond.common.notification.NotificationEntity;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseSecurityResource;

import lombok.extern.slf4j.Slf4j;

@Path("/email")
@Slf4j
public class EmailResource extends BaseSecurityResource<NotificationEntity, UserEntity> {

    @Inject
    EmailService emailService;

    @POST
    @Path("/produce")
    public Response produce(@Context SecurityContext contexte) {
        UserEntity userConnected = getUserConnected();
        log.info("{}", userConnected.username);
        EmailRequest mail = emailService.sendEmailOutgoing(new EmailRequest(userConnected.email, "test", "test", userConnected));
        return Response.ok(mail).build();
    }

}
