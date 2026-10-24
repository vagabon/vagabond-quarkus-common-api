package org.vagabond.common.auth.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.internal.util.Assert;
import org.vagabond.engine.auth.annotation.AuthRole;
import org.vagabond.engine.auth.annotation.AuthSecure;
import org.vagabond.engine.auth.service.BaseAuthService;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.gateway.GatewayUser;

@AuthSecure
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 2)
public class AuthInterceptor {

    @Inject
    private GatewayUser gatewayUser;

    @AroundInvoke
    @SuppressWarnings({ "null", "unused" })
    public Object validateToken(InvocationContext ctx) throws Exception {
        Assert.notNull(gatewayUser);

        var method = ctx.getMethod();

        var authRole = method.getAnnotation(AuthRole.class);
        if (authRole == null) {
            authRole = ctx.getTarget().getClass().getAnnotation(AuthRole.class);
        }
        if (authRole != null) {
            String role = authRole.value();
            if (StringUtils.isNotBlank(role)) {
                if (gatewayUser.get() == null || !gatewayUser.get().hasRole(role)) {
                    throw new MetierException(BaseAuthService.USER_NOT_CONNECTED);
                }
            }
        }
        return ctx.proceed();
    }
}