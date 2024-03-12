package org.vagabond.common.news.payload;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import org.vagabond.common.user.payload.UserResponse;

@Getter
@Setter
public class NewsResponse {

    private Long id;
    private String title;
    private String avatar;
    private String image;
    public String resume;
    private String description;
    public String tags;
    private UserResponse user;
    public LocalDateTime creationDate;

}
