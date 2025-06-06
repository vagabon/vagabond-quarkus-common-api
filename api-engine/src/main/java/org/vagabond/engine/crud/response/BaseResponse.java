package org.vagabond.engine.crud.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse {
    public Long id;
    public LocalDateTime creationDate;
}
