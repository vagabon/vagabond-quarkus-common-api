package org.vagabond.common.file.payload;

import org.vagabond.engine.crud.response.BaseResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse extends BaseResponse {

    public String path;

    public Long userId;

}
