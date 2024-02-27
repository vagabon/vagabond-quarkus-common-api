package org.vagabond.common.auth;

import java.util.Map;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.vagabond.common.auth.payload.request.ActivationRequest;
import org.vagabond.common.auth.payload.request.EmailRequest;
import org.vagabond.common.auth.payload.request.FacebookRequest;
import org.vagabond.common.auth.payload.request.GoogleRequest;
import org.vagabond.common.auth.payload.response.FacebookResponse;
import org.vagabond.common.auth.payload.response.GoogleResponse;
import org.vagabond.common.auth.service.AuthService;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.auth.BaseAuthResource;
import org.vagabond.engine.http.HttpComponent;
import org.vagabond.engine.mapper.MapperUtils;

@RunOnVirtualThread
@Path("/auth")
public class AuthResource extends BaseAuthResource<UserEntity, ProfileEntity> {

    private static final String TOKEN = "token";
    private static final String URL_GOOGLE = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    private static final String URL_FACEBOOK = "https://graph.facebook.com/v9.0/me?&fields=name,email,picture&method=get&pretty=0&sdk=joey&suppress_http_code=1&access_token=";

    @Inject
    AuthService authService;

    @Inject
    HttpComponent httpComponent;

    @POST
    @Path("/activation")
    public Response activationUser(ActivationRequest activationRequest) {
        authService.activationUser(activationRequest.token());
        return responseOkJson();
    }

    @POST
    @Path("/createIdentityToken")
    public Response createIdentityToken(@RequestBody EmailRequest emailRequest) {
        authService.createIdentityToken(emailRequest.email());
        return responseOkJson();
    }

    @POST
    @Path("/checkIdentityToken")
    public Response checkIdentityToken(@RequestBody ActivationRequest activationRequest) {
        var user = authService.checkIdentityToken(activationRequest.token());
        return responseOk(Map.of(TOKEN, user.identityToken));
    }

    @POST
    @Path("/resetPassword")
    public Response resetPassword(@RequestBody ActivationRequest activationRequest) {
        authService.resetPassword(activationRequest.token());
        return responseOk(Map.of(TOKEN, activationRequest.token()));
    }

    @POST
    @Path("/google-connect")
    @Transactional
    public Response googleConnect(GoogleRequest googleRequest) {
        var url = URL_GOOGLE + googleRequest.googleToken();
        var googleResponse = httpComponent.httpGet(url, GoogleResponse.class);
        UserEntity user = authService.googleConnect(googleResponse);
        return getJwtTokens(user);
    }

    @POST
    @Path("/facebook-connect")
    @Transactional
    public Response facebookConnect(@RequestBody FacebookRequest facebookRequest) {
        String url = URL_FACEBOOK + facebookRequest.accessToken();
        var facebookResponse = httpComponent.httpGet(url, FacebookResponse.class);
        UserEntity user = authService.facebookConnect(facebookResponse);
        return getJwtTokens(user);
    }

    @Override
    public AuthService getService() {
        return authService;
    }

    @Override
    protected Object toDto(UserEntity user) {
        return MapperUtils.toDto(user, UserResponse.class);
    }
}
