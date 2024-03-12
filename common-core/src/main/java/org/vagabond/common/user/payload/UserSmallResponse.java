package org.vagabond.common.user.payload;

import lombok.Getter;
import lombok.Setter;
import org.vagabond.engine.crud.dto.BaseResponse;

@Getter
@Setter
public class UserSmallResponse extends BaseResponse {

    public String username;
    public String avatar;
}
