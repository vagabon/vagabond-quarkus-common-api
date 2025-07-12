package org.vagabond.common.api.auth;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.vagabond.common.auth.payload.request.ActivationRequest;
import org.vagabond.common.auth.payload.request.AuthEmailRequest;
import org.vagabond.common.auth.payload.request.FacebookRequest;
import org.vagabond.common.auth.payload.request.GoogleRequest;
import org.vagabond.common.auth.payload.response.CaptchaResponse;
import org.vagabond.common.auth.payload.response.FacebookResponse;
import org.vagabond.common.auth.payload.response.GoogleIdentityResponse;
import org.vagabond.common.auth.payload.response.GoogleResponse;
import org.vagabond.common.auth.service.AuthService;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.auth.BaseAuthResource;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.http.HttpComponent;
import org.vagabond.engine.mapper.MapperUtils;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/auth")
@SecurityRequirement(name = "SecurityScheme")
@RunOnVirtualThread
public class AuthResource extends BaseAuthResource<UserEntity, ProfileEntity> {

    private static final String TOKEN = "token";
    private static final String URL_GOOGLE = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    private static final String URL_GOOGLE_IDENTITY = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private static final String URL_FACEBOOK = "https://graph.facebook.com/v9.0/me?&fields=name,email,picture&method=get&pretty=0&sdk=joey&suppress_http_code=1&access_token=";
    private static final String URL_CAPTCHA = "https://www.google.com/recaptcha/api/siteverify?secret=";

    @ConfigProperty(name = "api.captcha.token", defaultValue = "NO_TOKEN")
    public String captchaPrivateKey;

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
    public Response createIdentityToken(@RequestBody AuthEmailRequest emailRequest) {
        authService.createIdentityToken(emailRequest.email());
        return responseOkJson();
    }

    @POST
    @Path("/checkIdentityToken")
    public Response checkIdentityToken(@RequestBody ActivationRequest activationRequest) {
        var identityToken = authService.checkIdentityToken(activationRequest.token());
        return responseOk(Map.of(TOKEN, identityToken));
    }

    @POST
    @Path("/resetPassword")
    public Response resetPassword(@RequestBody ActivationRequest activationRequest) {
        authService.resetPassword(activationRequest.token());
        return responseOk(Map.of(TOKEN, activationRequest.token()));
    }

    @POST
    @Path("/google-connect")
    @WithTransaction
    public Response googleConnect(GoogleRequest googleRequest) {
        var url = URL_GOOGLE + googleRequest.googleToken();
        var googleResponse = httpComponent.httpGet(url, GoogleResponse.class);
        UserEntity user = authService.googleConnect(googleResponse);
        return getJwtTokens(user);
    }

    @POST
    @Path("/google-identity-connect")
    @WithTransaction
    public Response googleConnectIdentity(GoogleRequest googleRequest) {
        var url = URL_GOOGLE_IDENTITY + googleRequest.googleToken();
        var googleIdentityResponse = httpComponent.httpGet(url, GoogleIdentityResponse.class);
        var googleResponse = new GoogleResponse();
        googleResponse.id = googleIdentityResponse.sub;
        googleResponse.givenName = googleIdentityResponse.givenName;
        googleResponse.email = googleIdentityResponse.email;
        googleResponse.picture = googleIdentityResponse.picture;
        UserEntity user = authService.googleConnect(googleResponse);
        return getJwtTokens(user);
    }

    @POST
    @Path("/facebook-connect")
    @WithTransaction
    public Response facebookConnect(@RequestBody FacebookRequest facebookRequest) {
        String url = URL_FACEBOOK + facebookRequest.accessToken();
        var facebookResponse = httpComponent.httpGet(url, FacebookResponse.class);
        UserEntity user = authService.facebookConnect(facebookResponse);
        return getJwtTokens(user);
    }

    @POST
    @Path("/captcha")
    @WithTransaction
    public Response validateCaptcha(@RequestBody ActivationRequest activationRequest) {
        String url = URL_CAPTCHA + captchaPrivateKey + "&response=" + activationRequest.token();
        var httpReponse = httpComponent.httpPost(url, "{}", CaptchaResponse.class);
        if (BooleanUtils.isNotTrue(httpReponse.success())) {
            throw new MetierException("ERRORS:CAPTCHA_INVALID");
        }
        return responseOk(httpReponse);
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
