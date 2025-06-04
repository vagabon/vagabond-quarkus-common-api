package org.vagabond.common.auth.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.modelmapper.internal.util.Assert;
import org.vagabond.common.auth.service.AuthService;
import org.vagabond.engine.auth.annotation.AuthRole;
import org.vagabond.engine.auth.annotation.AuthSecure;

@AuthSecure
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
public class AuthInterceptor {

  @Inject
  private JsonWebToken jwt;

  @Inject
  private AuthService authService;

  @AroundInvoke
  public Object validateToken(InvocationContext ctx) throws Exception {
    Assert.notNull(jwt);

    var method = ctx.getMethod();

    var authRole = method.getAnnotation(AuthRole.class);
    if (authRole == null) {
      authRole = ctx.getTarget().getClass().getAnnotation(AuthRole.class);
    }
    if (authRole != null) {
      String role = authRole.value();
      if (StringUtils.isNotBlank(role)) {
        var user = authService.findByUsername(jwt.getName());
        authService.hasRole(user, role);
      }
    }
    return ctx.proceed();
  }
}