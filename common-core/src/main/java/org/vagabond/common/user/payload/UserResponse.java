package org.vagabond.common.user.payload;

import java.util.List;

import org.vagabond.common.profile.payload.ProfileResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends UserLightResponse {

    public List<ProfileResponse> profiles;
}
