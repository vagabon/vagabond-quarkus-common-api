package org.vagabond.common.api.user;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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
import org.vagabond.engine.auth.entity.BaseProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.crud.resource.BaseUploadResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/user")
@RunOnVirtualThread
public class UserResource extends BaseUploadResource<UserEntity> {

    public static final String UPLOAD_DIRECTORY = "/user";

    @Inject
    private UserService userService;

    @PostConstruct
    public void postConstruct() {
        service = userService;
        roleRead = ADMIN;
        roleModify = ADMIN;
        responseClass = UserResponse.class;
    }

    @Override
    public void doBeforeUpload(BaseUserEntity<BaseProfileEntity> userConnected, Long id) {
        verifyUserConnected(userConnected, id);
    }

    @Override
    public void doAfterUpload(Long id, String image) {
        UserEntity user = userService.findById(id);
        user.avatar = image;
        userService.persist(user);
    }

    @Override
    public String getDirectoryName() {
        return UPLOAD_DIRECTORY;
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

    @GET
    @Path("/find50")
    public Response findTop50(@QueryParam("username") String username) {
        return responseOk(userService.findTop50(username));
    }
}