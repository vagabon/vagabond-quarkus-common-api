package org.vagabond.common.api.file;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.vagabond.common.file.FileEntity;
import org.vagabond.common.file.FileService;
import org.vagabond.common.file.payload.FileResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseCrudResource;

import io.smallrye.common.annotation.RunOnVirtualThread;

@Path("/file")
@RunOnVirtualThread
public class FileResource extends BaseCrudResource<FileEntity, UserEntity> {

    @Inject
    FileService fileService;

    @PostConstruct
    public void postConstruct() {
        service = fileService;
        responseClass = FileResponse.class;
    }

    @GET()
    @Path("/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileFromLocal(@QueryParam("fileName") String fileName) {
        var file = fileService.downloadFile(fileName);
        var response = Response.ok(file);
        response.header("Content-Disposition", "attachment;filename=" + file);
        return response.build();
    }

    @POST()
    @Path(value = "/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public Response handleFileUpload(@QueryParam(value = "directory") String directory, MultipartFormDataInput fileForm) {
        var userConnected = getUserConnected();

        var entry = fileForm.getValues().entrySet().stream().toList();
        var firstFile = entry.get(0).getValue().stream().findFirst().orElseThrow();
        var fileName = firstFile.getFileName();

        var file = fileService.findByFilename(fileName, userConnected.id);
        if (file == null) {
            String image = fileService.uploadFile(firstFile, directory);
            file = fileService.create(fileName, directory, image, userConnected);
        }

        return responseOk(file);
    }
}
