package org.vagabond.common.news.payload;

import java.time.LocalDateTime;

import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsResponse extends BaseResponse {

    private String title;
    private String avatar;
    private String image;
    public String resume;
    private String description;
    public String tags;
    private UserResponse user;
    public LocalDateTime creationDate;

}
