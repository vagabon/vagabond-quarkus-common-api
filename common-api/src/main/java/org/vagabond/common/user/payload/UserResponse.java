package org.vagabond.common.user.payload;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.vagabond.common.profile.payload.ProfileResponse;

@Getter
@Setter
public class UserResponse {

    public Long id;
    public String username;
    public String avatar;
    public String googleId;
    public String facebookId;

    public List<ProfileResponse> profiles;
}
