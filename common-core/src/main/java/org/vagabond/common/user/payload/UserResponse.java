package org.vagabond.common.user.payload;

import java.util.List;

import org.vagabond.common.profile.payload.ProfileResponse;
import org.vagabond.engine.crud.dto.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseResponse {

    public String username;
    public String avatar;
    public String googleId;
    public String facebookId;

    public List<ProfileResponse> profiles;
}
