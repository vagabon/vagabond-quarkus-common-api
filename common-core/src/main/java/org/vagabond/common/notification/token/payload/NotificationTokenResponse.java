package org.vagabond.common.notification.token.payload;

import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTokenResponse extends BaseResponse {

    private Long userId;
    private String token;

}
