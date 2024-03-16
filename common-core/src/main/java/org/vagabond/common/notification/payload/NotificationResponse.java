package org.vagabond.common.notification.payload;

import java.time.LocalDateTime;

import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.crud.dto.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse extends BaseResponse {

    private UserResponse user;
    private String title;
    private String message;
    private String url;
    private Long entityId;
    private String users;
    private String type;
    private String type2;
    public LocalDateTime creationDate;
}
