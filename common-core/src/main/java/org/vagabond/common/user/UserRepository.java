package org.vagabond.common.user;

import java.time.LocalDateTime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.exeption.MetierException;

@ApplicationScoped
public class UserRepository extends BaseRepository<UserEntity> {

    @Transactional
    public UserEntity getUserFromIdentityToken(String token) {
        var entity = findBy("where identityToken = ?1 and identityTokenDateEnd >= ?2", token, LocalDateTime.now());
        if (entity == null || entity.isEmpty()) {
            throw new MetierException(ENTITY_NOT_FOUND_EXCEPTION);
        }
        return entity.get(0);
    }
}
