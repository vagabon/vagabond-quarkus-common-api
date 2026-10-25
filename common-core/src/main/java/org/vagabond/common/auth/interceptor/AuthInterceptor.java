package org.vagabond.common.auth.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.internal.util.Assert;
import org.vagabond.engine.auth.decorator.AuthRole;
import org.vagabond.engine.auth.decorator.AuthSecure;
import org.vagabond.engine.crud.resource.BaseSecurityResource;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.gateway.user.GatewayUser;

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
                if (gatewayUser.get() == null) {
                    throw new MetierException(BaseSecurityResource.USER_NOT_CONNECTED);
                } else if (!gatewayUser.get().hasRole(role)) {
                    throw new MetierException(BaseSecurityResource.USER_NOT_ALLOWED);
                }
            }
        }
        return ctx.proceed();
    }
}