package org.vagabond.common.profile.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {

    private Long id;
    private String name;
    private String roles;
}
