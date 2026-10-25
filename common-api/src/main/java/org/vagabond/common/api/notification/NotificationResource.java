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

import org.vagabond.common.notification.entity.NotificationEntity;
import org.vagabond.common.notification.payload.NotificationRequest;
import org.vagabond.common.notification.payload.NotificationResponse;
import org.vagabond.common.notification.service.NotificationService;
import org.vagabond.engine.auth.decorator.AuthRole;
import org.vagabond.engine.auth.decorator.AuthSecure;
import org.vagabond.engine.crud.resource.BaseCrudResource;
import org.vagabond.engine.crud.response.PageResponse;
import org.vagabond.engine.user.CurrentUser;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/notifications")
@RunOnVirtualThread
public class NotificationResource
    extends BaseCrudResource<NotificationEntity> {

    @Inject
    NotificationService notificationService;

    @Override
    public void doBeforeCreate(
        CurrentUser userConnected,
        NotificationEntity notification) {
        verifyUserConnected(userConnected, notification.userId);
    }

    @Override
    public void doBeforeUpdate(
        CurrentUser userConnected,
        NotificationEntity notification) {
        verifyUserConnected(userConnected, notification.userId);
    }

    @Override
    public void doBeforeDelete(
        CurrentUser userConnected,
        NotificationEntity notification) {
        verifyUserConnected(userConnected, notification.userId);
    }

    @PostConstruct
    public void postConstruct() {
        service = notificationService;
        responseClass = NotificationResponse.class;
    }

    @GET
    @Path("/search")
    @AuthSecure
    @AuthRole("USER")
    public PageResponse<NotificationResponse> findBy(
        @DefaultValue("") @QueryParam("search") String search,
        @DefaultValue("") @QueryParam("category") String category,
        @DefaultValue("") @QueryParam("type") String type,
        @QueryParam("entityId") Long entityId,
        @DefaultValue("1") @QueryParam("page") Integer page) {
        var userConnected = getUserConnected();
        return notificationService.search(userConnected.id(), category, type, entityId,
            search, page - 1);
    }

    @GET
    @Path("/count")
    @AuthSecure
    @AuthRole("USER")
    public Response countByUserConnected() {
        var userConnected = getUserConnected();
        return responseOk(notificationService.countNotReadByUser(userConnected.id()));
    }

    @GET
    @Path("/count/{userId}")
    public Response countByCreatorId(Long userId) {
        return responseOk(notificationService.countNotReadByUser(userId));
    }

    @PUT
    @Path("/read-all")
    @AuthSecure
    @AuthRole("USER")
    public Response readByUserConnected() {
        var userConnected = getUserConnected();
        return responseOk(notificationService.readAll(userConnected.id()));
    }

    @PUT
    @Path("/read/{notificationId}")
    @AuthSecure
    @AuthRole("USER")
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
    @AuthRole("USER")
    public Response sendNotification() {
        var userConnected = getUserConnected();
        var notification = new NotificationRequest("test", "test", "/notification");
        notificationService.sendNotification(userConnected,
            Arrays.asList(userConnected.id()), notification,
            null, "test", "test", "test");
        return responseOkJson();
    }
}