package org.vagabond.engine.auth;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.engine.auth.entity.BaseProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.auth.entity.BaseUserTokenEntity;
import org.vagabond.engine.auth.payload.request.AuthRequest;
import org.vagabond.engine.auth.payload.response.AuthResponse;
import org.vagabond.engine.auth.service.BaseAuthService;
import org.vagabond.engine.auth.service.BaseAuthTokenService;
import org.vagabond.engine.crud.resource.BaseResource;
import org.vagabond.engine.exeption.MetierException;

import io.smallrye.common.annotation.RunOnVirtualThread;

@RunOnVirtualThread
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class BaseAuthResource<U extends BaseUserEntity<P>, P extends BaseProfileEntity, T extends BaseUserTokenEntity>
        implements BaseResource {

    public static final String REFRESH_TOKEN_ERROR = "NO_REFRESH_TOKEN";
    private static final String REFRESH_TOKEN_URL = "/auth/refresh-token";

    @ConfigProperty(name = "api.cookie.name", defaultValue = "refresh-token")
    private String cookieName;

    @POST
    @Path("/signin")

    @PermitAll
    public Response signin(AuthRequest authRequest) {
        U user = getService().signIn(authRequest.username(), authRequest.password());
        return getJwtTokens(user);
    }

    @POST
    @Path("/refresh-token")
    @PermitAll
    public Response refreshToken(@Context HttpHeaders headers) {
        var cookie = headers.getCookies().get(cookieName);
        if (cookie == null) {
            throw new MetierException(REFRESH_TOKEN_ERROR);
        }
        String refreshToken = headers.getCookies().get(cookieName).getValue();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new MetierException(REFRESH_TOKEN_ERROR);
        }
        var user = getAuthTokenService().findUserByRefreshToken(refreshToken);
        if (user == null) {
            throw new MetierException(REFRESH_TOKEN_ERROR);
        }
        getService().resetConnectionTrials(user);
        getAuthTokenService().deleteRefreshToken(refreshToken);
        return getJwtTokens(user);
    }

    @POST
    @Path("/logout")
    @PermitAll
    public Response logout() {
        var deleteCookie = createCookieRefresh("", 0);
        return Response.ok().cookie(deleteCookie).build();
    }

    protected Response getJwtTokens(U user) {
        var token = getService().generateTokenJwt(user);
        var refreshToken = getAuthTokenService().generateRefreshToken(user);
        var refreshCookie = createCookieRefresh(refreshToken,
                getAuthTokenService().getRefreshTokenTtl());

        return Response.ok(new AuthResponse<>(toDto(user), token)).cookie(refreshCookie).build();
    }

    private NewCookie createCookieRefresh(String refreshToken, int tll) {
        return new NewCookie.Builder(cookieName).value(refreshToken).httpOnly(true)
                .sameSite(NewCookie.SameSite.LAX).path(REFRESH_TOKEN_URL).maxAge(tll).build();

    }

    @PermitAll
    @POST
    @Path("/signup")
    public Response signup(U user) {
        return responseOk(toDto(getService().signup(user)));
    }

    protected abstract Object toDto(U user);

    public abstract BaseAuthService<U, P> getService();

    public abstract BaseAuthTokenService<T, U> getAuthTokenService();
}
