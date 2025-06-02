package org.vagabond.engine.auth;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.vagabond.engine.auth.entity.BaseProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.auth.payload.request.AuthRequest;
import org.vagabond.engine.auth.payload.request.RefreshTokenRequest;
import org.vagabond.engine.auth.payload.response.AuthResponse;
import org.vagabond.engine.auth.service.BaseAuthService;
import org.vagabond.engine.crud.resource.BaseResource;
import org.vagabond.engine.exeption.MetierException;

import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;

@RunOnVirtualThread
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class BaseAuthResource<T extends BaseUserEntity<P>, P extends BaseProfileEntity> implements BaseResource {

    private static final Long REFRESH_TTL = 1000000000000000000L;

    @Inject
    protected JWTParser parser;

    @PermitAll
    @POST
    @Path("/signin")
    public Response signin(AuthRequest authRequest) {
        var user = getService().signIn(authRequest.username(), authRequest.password());
        return getJwtTokens(user);
    }

    @PermitAll
    @POST
    @Path("/refresh-token")
    @Transactional
    public Response refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException {
        try {
            var token = parser.parse(refreshTokenRequest.refreshToken());
            T user = getService().findByUsername(token.getName());
            getService().resetConnectionTrials(user);
            return getJwtTokens(user);
        } catch (ParseException e) {
            throw new MetierException("NO REFRESH TOKEN");
        }
    }

    protected Response getJwtTokens(T user) {
        var token = getService().generateTokenJwt(user);
        var refreshToken = getService().generateTokenJwt(user, REFRESH_TTL);
        return responseOk(new AuthResponse<Object>(toDto(user), token, refreshToken));
    }

    @PermitAll
    @POST
    @Path("/signup")
    public Response signup(T user) {
        return responseOk(toDto(getService().signup(user)));
    }

    protected abstract Object toDto(T user);

    public abstract BaseAuthService<T, P> getService();
}
