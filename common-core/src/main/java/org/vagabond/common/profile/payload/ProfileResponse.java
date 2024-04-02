package org.vagabond.common.profile.payload;

import java.time.LocalDateTime;

import org.vagabond.engine.crud.dto.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse extends BaseResponse {

    private String name;
    private String roles;
    public LocalDateTime endPlan;

}
