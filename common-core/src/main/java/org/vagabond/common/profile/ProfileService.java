package org.vagabond.common.profile;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

@ApplicationScoped
public class ProfileService extends BaseService<ProfileEntity> {

    @Inject
    ProfileRepository profileRepository;

    @Override
    public BaseRepository<ProfileEntity> getRepository() {
        return profileRepository;
    }

}