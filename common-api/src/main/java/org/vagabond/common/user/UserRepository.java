package org.vagabond.common.user;

import org.vagabond.engine.crud.repository.BaseRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository extends BaseRepository<UserEntity> {

    public UserEntity getUserFromIdentityToken(String token) {
        return findByOrThrow("identityToken", token);
    }
}
