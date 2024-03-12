package org.vagabond.common.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class UserRepository extends BaseRepository<UserEntity> {

    @Transactional
    public UserEntity getUserFromIdentityToken(String token) {
        return findByOrThrow("identityToken", token); // TODO : check date end de l'identity token
    }
}
