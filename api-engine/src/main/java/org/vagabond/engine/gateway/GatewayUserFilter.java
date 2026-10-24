package org.vagabond.engine.gateway;

import java.util.Set;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Interceptor.Priority.APPLICATION + 1)
public class GatewayUserFilter implements ContainerRequestFilter {

    @Inject
    GatewayUser gatewayUser;

    @Override
    public void filter(ContainerRequestContext ctx) {
        var id = ctx.getHeaderString("X-User-Id");

        if (id == null || id.isBlank()) {
            return;
        }

        var roles = ctx.getHeaderString("X-User-Roles");
        gatewayUser.set(new CurrentUser(
            Long.valueOf(id),
            ctx.getHeaderString("X-User-Name"),
            ctx.getHeaderString("X-User-Email"),
            roles == null || roles.isBlank()
                ? Set.of()
                : Set.of(roles.split(","))));
    }
}