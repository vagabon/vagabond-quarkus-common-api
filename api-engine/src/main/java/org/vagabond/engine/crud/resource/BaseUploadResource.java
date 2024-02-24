package org.vagabond.engine.crud.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.vagabond.engine.auth.entity.BaseProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;
import org.vagabond.engine.crud.service.UploadService;

public abstract class BaseUploadResource<T extends BaseCrudEntity> extends BaseCrudResource<T> {

    @Inject
    UploadService uploadService;

    @POST()
    @Path(value = "/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response handleFileUpload(@Context SecurityContext contexte, @QueryParam(value = "id") Long id, MultipartFormDataInput file) {
        var userConnected = hasRole(contexte, "USER");
        doBeforeUpload(userConnected, id);
        var entry = file.getValues().entrySet().stream().toList();
        var firstFile = entry.get(0).getValue().stream().findFirst().orElseThrow();
        String image = uploadService.uploadFile(firstFile, getDirectoryName(), id);
        doAfterUpload(id, image);
        return responseOk(image);
    }

    public void doBeforeUpload(BaseUserEntity<BaseProfileEntity> userConnected, Long id) {
    }

    public void doAfterUpload(Long id, String image) {
    }

    public abstract String getDirectoryName();
}
