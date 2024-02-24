package org.vagabond.common.profile;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import org.vagabond.common.profile.payload.ProfileResponse;
import org.vagabond.engine.crud.dto.PageResponse;
import org.vagabond.engine.crud.resource.BaseCrudResource;
import org.vagabond.engine.mapper.MapperUtils;

@Path("/profile")
@RunOnVirtualThread
public class ProfileResource extends BaseCrudResource<ProfileEntity> {

    @Inject
    ProfileService profileService;

    @PostConstruct
    public void postConstruct() {
        service = profileService;
    }

    @Override
    public Object toDto(ProfileEntity entity) {
        return MapperUtils.toDto(entity, ProfileResponse.class);
    }

    @Override
    public PageResponse toPage(PageResponse response) {
        return MapperUtils.toPage(response, ProfileResponse.class);
    }
}