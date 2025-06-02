package org.vagabond.common.auth.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.modelmapper.internal.util.Assert;
import org.vagabond.common.auth.service.AuthService;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.auth.annotation.AuthRole;
import org.vagabond.engine.auth.annotation.AuthSecure;
import org.vagabond.engine.crud.utils.SecurityUtils;
import org.vagabond.engine.exeption.MetierException;

import lombok.extern.slf4j.Slf4j;

@AuthSecure
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
@Slf4j
public class AuthInterceptor {

  private static final String USER_NOT_CONNECTED = "USER NOT CONNECTED";

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
        hasRole(user, role);
      }
    }
    return ctx.proceed();
  }

  @Transactional
  protected void hasRole(UserEntity user, String roles) {
    List<String> groups = new ArrayList<>();
    if (user != null) {
      groups = user.getProfiles().stream().map(profile -> profile.roles.split(",")).flatMap(Arrays::stream).toList();
    }
    if (StringUtils.isNotBlank(roles)) {
      SecurityUtils.hasRole(roles, groups);
    } else if (groups.isEmpty()) {
      throw new MetierException(USER_NOT_CONNECTED);
    }
  }
}