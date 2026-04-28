package org.vagabond.engine.crud.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse {
    public Long id;
    public Instant creationDate;
}
