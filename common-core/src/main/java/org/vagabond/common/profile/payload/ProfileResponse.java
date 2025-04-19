package org.vagabond.common.profile.payload;

import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse extends BaseResponse {

    private String name;
    private String roles;

}
