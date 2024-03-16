package org.vagabond.common.notification.token.payload;

import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.crud.dto.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTokenResponse extends BaseResponse {

    private UserResponse user;
    private String token;

}
