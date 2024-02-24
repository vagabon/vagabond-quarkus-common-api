package org.vagabond.common.upload;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vagabond.engine.crud.service.UploadService;

@Path("/download")
@RunOnVirtualThread
public class DownloadController {

    @Inject
    UploadService uploadService;

    @GET()
    @Path("/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileFromLocal(@QueryParam("fileName") String fileName) {
        var file = uploadService.downloadFile(fileName);
        var response = Response.ok(file);
        response.header("Content-Disposition", "attachment;filename=" + file);
        return response.build();
    }

}
