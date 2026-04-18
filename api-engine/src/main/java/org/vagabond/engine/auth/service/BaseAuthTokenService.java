package org.vagabond.engine.auth.service;

import org.vagabond.engine.crud.entity.BaseCrudEntity;
import org.vagabond.engine.crud.service.BaseService;

public abstract class BaseAuthTokenService<E extends BaseCrudEntity, U extends BaseCrudEntity>
        extends BaseService<E> {

    public abstract String generateRefreshToken(U user);

    public abstract void deleteRefreshToken(String refreshToken);

    public abstract U findUserByRefreshToken(String refreshToken);

    public abstract int getRefreshTokenTtl();
}
