package org.vagabond.common.file.payload;

import org.vagabond.common.user.payload.UserSmallResponse;
import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse extends BaseResponse {

    public String path;
    private UserSmallResponse user;

}
