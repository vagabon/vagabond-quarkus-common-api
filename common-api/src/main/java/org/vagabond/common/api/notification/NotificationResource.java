package org.vagabond.common.api.notification;

import java.util.Arrays;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.vagabond.common.notification.NotificationEntity;
import org.vagabond.common.notification.NotificationService;
import org.vagabond.common.notification.payload.NotificationRequest;
import org.vagabond.common.notification.payload.NotificationResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserService;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/notification")
@RunOnVirtualThread
public class NotificationResource extends BaseCrudResource<NotificationEntity> {

    @Inject
    NotificationService notificationService;

    @Inject
    UserService userService;

    @Override
    public void doBeforeCreate(Object userConnected, NotificationEntity notification) {
        verifyUserConnected((UserEntity) userConnected, notification.user.id);
    }

    @Override
    public void doBeforeUpdate(Object userConnected, NotificationEntity notification) {
        verifyUserConnected((UserEntity) userConnected, notification.user.id);
    }

    @Override
    public void doBeforeDelete(Object userConnected, NotificationEntity notification) {
        verifyUserConnected((UserEntity) userConnected, notification.user.id);
    }

    @PostConstruct
    public void postConstruct() {
        service = notificationService;
        roleRead = "USER";
        roleModify = "USER";
        responseClass = NotificationResponse.class;
    }

    @GET
    @Path("/count/{userId}")
    public Response countByCreatorId(Long userId) {
        return responseOk(notificationService.countByMemberOrCreator(userId));
    }

    @PUT
    @Path("/readAll/{userId}")
    public Response readAll(Long userId) {
        return responseOk(notificationService.readAll(userId));
    }

    @POST
    @Path("/send-notification/{userId}")
    public Response sendNotification(@Context SecurityContext contexte, Long userId) {
        var user = userService.findById(userId);
        var notification = new NotificationRequest("test", "test", "http://localhost:3003/todolist/");
        notificationService.sendNotification(user, Arrays.asList(userId), notification, null, "test", "test", "test");
        return responseOkJson();
    }
}