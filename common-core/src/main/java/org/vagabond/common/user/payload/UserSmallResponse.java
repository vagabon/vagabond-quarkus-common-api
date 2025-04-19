package org.vagabond.common.user.payload;

import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSmallResponse extends BaseResponse {

    public String username;
    public String avatar;
}
