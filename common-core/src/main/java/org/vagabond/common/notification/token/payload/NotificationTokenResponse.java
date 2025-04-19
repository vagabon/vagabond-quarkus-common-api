package org.vagabond.common.notification.token.payload;

import org.vagabond.common.user.payload.UserSmallResponse;
import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTokenResponse extends BaseResponse {

    private UserSmallResponse user;
    private String token;

}
