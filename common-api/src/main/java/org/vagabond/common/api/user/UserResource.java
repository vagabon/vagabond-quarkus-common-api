package org.vagabond.common.api.user;

import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserService;
import org.vagabond.common.user.payload.PasswordRequest;
import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/user")
@RunOnVirtualThread
public class UserResource extends BaseCrudResource<UserEntity, UserEntity> {

    @Inject
    private UserService userService;

    @PostConstruct
    public void postConstruct() {
        service = userService;
        roleRead = ADMIN;
        roleFindBy = ADMIN;
        roleModify = ADMIN;
        responseClass = UserResponse.class;
    }

    @PUT
    @Path("/email")
    public Response updateEmail(@Context SecurityContext contexte, @RequestBody UserEntity user) {
        UserEntity userConnected = hasRole(contexte, "USER");
        verifyUserConnected(userConnected, user.id);
        return responseOk(userService.updateEmail(user.id, user.email));
    }

    @PUT
    @Path("/password")
    public Response updatePassword(@Context SecurityContext contexte, @RequestBody PasswordRequest passwordRequest) {
        UserEntity userConnected = hasRole(contexte, "USER");
        verifyUserConnected(userConnected, passwordRequest.id());
        return responseOk(userService.updatePassword(passwordRequest.id(), passwordRequest.password(), passwordRequest.newPassword()));
    }

    @POST
    @Path("/avatar")
    public Response updateAvatar(@Context SecurityContext contexte, @RequestBody UserEntity user) {
        UserEntity userConnected = hasRole(contexte, "USER");
        verifyUserConnected(userConnected, user.id);
        userConnected.avatar = user.avatar;
        userConnected = userService.persist(userConnected);
        return responseOk(Map.of("id", userConnected.id));
    }

    @GET
    @Path("/find50")
    public Response findTop50(@QueryParam("username") String username) {
        return responseOk(userService.findTop50(username));
    }
}