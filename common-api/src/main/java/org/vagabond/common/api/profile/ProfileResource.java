package org.vagabond.common.api.profile;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.profile.ProfileService;
import org.vagabond.common.profile.payload.ProfileResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/profile")
@SecurityRequirement(name = "SecurityScheme")
@RunOnVirtualThread
public class ProfileResource extends BaseCrudResource<ProfileEntity, UserEntity> {

    @Inject
    ProfileService profileService;

    @PostConstruct
    public void postConstruct() {
        service = profileService;
        responseClass = ProfileResponse.class;
    }
}