package org.vagabond.common.api.notification;

import java.util.Arrays;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.vagabond.common.notification.entity.NotificationEntity;
import org.vagabond.common.notification.payload.NotificationRequest;
import org.vagabond.common.notification.payload.NotificationResponse;
import org.vagabond.common.notification.service.NotificationService;
import org.vagabond.common.user.entity.UserEntity;
import org.vagabond.common.user.service.UserService;
import org.vagabond.engine.auth.annotation.AuthRole;
import org.vagabond.engine.auth.annotation.AuthSecure;
import org.vagabond.engine.crud.resource.BaseCrudResource;
import org.vagabond.engine.crud.response.PageResponse;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/notification")
@SecurityRequirement(name = "SecurityScheme")
@RunOnVirtualThread
public class NotificationResource extends BaseCrudResource<NotificationEntity, UserEntity> {

    @Inject
    NotificationService notificationService;

    @Inject
    UserService userService;

    @Override
    public void doBeforeCreate(UserEntity userConnected, NotificationEntity notification) {
        verifyUserConnected(userConnected, notification.user.id);
    }

    @Override
    public void doBeforeUpdate(UserEntity userConnected, NotificationEntity notification) {
        verifyUserConnected(userConnected, notification.user.id);
    }

    @Override
    public void doBeforeDelete(UserEntity userConnected, NotificationEntity notification) {
        verifyUserConnected(userConnected, notification.user.id);
    }

    @PostConstruct
    public void postConstruct() {
        service = notificationService;
        responseClass = NotificationResponse.class;
    }

    @GET
    @Path("/search")
    @AuthSecure
    @AuthRole("user")
    public PageResponse<NotificationEntity> findBy(@DefaultValue("") @QueryParam("search") String search,
            @DefaultValue("") @QueryParam("category") String category,
            @DefaultValue("") @QueryParam("type") String type, @QueryParam("entityId") Long entityId,
            @DefaultValue("1") @QueryParam("page") Integer page) {
        var userConnected = getUserConnected();
        return notificationService.search(userConnected.id, category, type, entityId, search, page - 1);
    }

    @GET
    @Path("/count")
    @AuthSecure
    @AuthRole("user")
    public Response countByUserConnected() {
        var userConnected = getUserConnected();
        return responseOk(notificationService.countNotReadByUser(userConnected.id));
    }

    @GET
    @Path("/count/{userId}")
    public Response countByCreatorId(Long userId) {
        return responseOk(notificationService.countNotReadByUser(userId));
    }

    @PUT
    @Path("/read-all")
    @AuthSecure
    @AuthRole("user")
    public Response readByUserConnected() {
        var userConnected = getUserConnected();
        return responseOk(notificationService.readAll(userConnected.id));
    }

    @PUT
    @Path("/read/{notificationId}")
    @AuthSecure
    @AuthRole("user")
    public Response readAllByUserConnected(Long notificationId) {
        return responseOk(notificationService.read(notificationId));
    }

    @PUT
    @Path("/readAll/{userId}")
    public Response readAll(Long userId) {
        return responseOk(notificationService.readAll(userId));
    }

    @POST
    @Path("/send")
    @AuthSecure
    @AuthRole("user")
    public Response sendNotification() {
        UserEntity userConnected = getUserConnected();
        var notification = new NotificationRequest("test", "test", "/notification");
        notificationService.sendNotification(userConnected, Arrays.asList(userConnected.id), notification,
                null, "test", "test", "test");
        return responseOkJson();
    }
}