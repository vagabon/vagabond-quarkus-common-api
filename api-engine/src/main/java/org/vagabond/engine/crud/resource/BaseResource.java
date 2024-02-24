package org.vagabond.engine.crud.resource;

import java.util.Map;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RunOnVirtualThread
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract interface BaseResource {

    public static final String ADMIN = "ADMIN";

    default Response responseOk(Object object) {
        return Response.ok(object).build();
    }

    default Response responseOkJson() {
        return responseOk(Map.of("OK", true));
    }

}
