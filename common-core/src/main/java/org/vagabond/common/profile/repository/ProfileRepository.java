package org.vagabond.common.profile.repository;

import jakarta.enterprise.context.ApplicationScoped;

import org.vagabond.common.profile.entity.ProfileEntity;
import org.vagabond.engine.crud.repository.BaseRepository;

@ApplicationScoped
public class ProfileRepository extends BaseRepository<ProfileEntity> {

    private static final String NAME = "name";
    private static final String USER_PROFILE = "USER";

    public ProfileEntity getProfileUser() {
        return findByOneField(NAME, USER_PROFILE);
    }
}
