package org.vagabond.common.notification.payload;

import java.time.LocalDateTime;

import org.vagabond.common.user.payload.UserSmallResponse;
import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse extends BaseResponse {

    public LocalDateTime creationDate;

    private String title;
    private String message;
    private String url;

    private String superType;
    public String category;
    private String type;

    private Long entityId;

    private String users;

    private Boolean read;

    private UserSmallResponse user;
}
