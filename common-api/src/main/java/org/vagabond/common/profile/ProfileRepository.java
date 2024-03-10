package org.vagabond.common.profile;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class ProfileRepository extends BaseRepository<ProfileEntity> {

    private static final String NAME = "name";
    private static final String USER_PROFILE = "USER";

    public ProfileEntity getProfileUser() {
        return findByOneField(NAME, USER_PROFILE);
    }
}
