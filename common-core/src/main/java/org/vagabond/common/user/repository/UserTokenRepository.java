package org.vagabond.common.user.repository;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.user.entity.UserTokenEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class UserTokenRepository extends BaseRepository<UserTokenEntity> {

    public UserTokenEntity findByToken(String token) {
        return find("token = ?1 and revoked = false and expiredDate > ?2", token,
                LocalDateTime.now()).firstResultOptional().orElse(null);
    }

    public long deleteRefreshToken(String token) {
        return deleteBy("token = ?1", token);
    }

    public long deleteExpiredTokens() {
        return deleteBy("expiredDate < ?1", LocalDateTime.now());
    }
}
