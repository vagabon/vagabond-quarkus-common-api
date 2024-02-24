package org.vagabond.common.profile;

import org.vagabond.engine.crud.repository.BaseRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfileRepository extends BaseRepository<ProfileEntity> {

    private static final String NAME = "name";
    private static final String USER_PROFILE = "USER";

    public ProfileEntity getProfileUser() {
        return findBy(NAME, USER_PROFILE);
    }
}
