package org.vagabond.common.api.user;

import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserService;
import org.vagabond.common.user.payload.PasswordRequest;
import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.auth.annotation.AuthRole;
import org.vagabond.engine.auth.annotation.AuthSecure;
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
        responseClass = UserResponse.class;
    }

    @PUT
    @Path("/email")
    @AuthSecure
    @AuthRole("USER")
    public Response updateEmail(@RequestBody UserEntity user) {
        UserEntity userConnected = getUserConnected();
        verifyUserConnected(userConnected, user.id);
        return responseOk(userService.updateEmail(user.id, user.email));
    }

    @PUT
    @Path("/password")
    @AuthSecure
    @AuthRole("USER")
    public Response updatePassword(@RequestBody PasswordRequest passwordRequest) {
        UserEntity userConnected = getUserConnected();
        verifyUserConnected(userConnected, passwordRequest.id());
        return responseOk(userService.updatePassword(passwordRequest.id(), passwordRequest.password(), passwordRequest.newPassword()));
    }

    @POST
    @Path("/avatar")
    @AuthSecure
    @AuthRole("USER")
    public Response updateAvatar(@RequestBody UserEntity user) {
        UserEntity userConnected = getUserConnected();
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