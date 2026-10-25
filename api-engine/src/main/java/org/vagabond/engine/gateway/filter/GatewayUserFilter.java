package org.vagabond.engine.gateway.filter;

import java.util.Set;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import org.vagabond.engine.gateway.user.GatewayUser;
import org.vagabond.engine.user.CurrentUser;

@Provider
@Priority(Interceptor.Priority.APPLICATION + 1)
public class GatewayUserFilter implements ContainerRequestFilter {

    @Inject
    GatewayUser gatewayUser;

    @Override
    public void filter(ContainerRequestContext ctx) {
        var id = ctx.getHeaderString("X-User-Id");
        var name = ctx.getHeaderString("X-User-Name");
        var email = ctx.getHeaderString("X-User-Email");
        var roles = ctx.getHeaderString("X-User-Roles");

        if (id == null || id.isBlank()) {
            gatewayUser.set(null);
            return;
        }

        gatewayUser.set(new CurrentUser(
            Long.valueOf(id),
            name,
            email,
            roles == null || roles.isBlank()
                ? Set.of()
                : Set.of(roles.split(","))));
    }
}