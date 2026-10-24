package org.vagabond.engine.exeption.handler;

import java.time.Instant;
import java.util.Arrays;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.vagabond.engine.crud.resource.BaseSecurityResource;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.exeption.dto.ExceptionResponse;

import io.quarkus.logging.Log;

@Provider
public class ExceptionHandler implements ExceptionMapper<RuntimeException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(RuntimeException exception) {
        var message = exception.getMessage();

        var uri = uriInfo != null ? uriInfo.getRequestUri() : null;
        var endpoint = uri != null ? uri.getPath() : "unknown";

        if (exception instanceof ClientWebApplicationException clientEx) {
            var status = clientEx.getResponse().getStatus();
            if (status == 404) {
                Log.errorf("Resource not found for endpoint : %s - message : %s",
                    endpoint,
                    clientEx.getMessage());
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

        var httpStatus = Response.Status.BAD_REQUEST;

        if (exception instanceof MetierException
            || exception instanceof NotFoundException) {
            var stackTraces = exception.getStackTrace();
            var stackTraceElements = Arrays.stream(stackTraces)
                .filter(trace -> trace.getClassName().contains("org.vagabond"))
                .toArray(StackTraceElement[]::new);
            var stack = new StackTraceElement[] {};
            if (stackTraceElements.length > 0) {
                stack = new StackTraceElement[] { stackTraceElements[0] };
            }
            exception.setStackTrace(stack);
        }

        if (exception instanceof ConstraintViolationException) {
            message = "ERRORS.CONTRAINTS_VIOLATION";
        }

        if (exception instanceof NotFoundException) {
            Log.errorf("No matching resource for endpoint: %s", endpoint);
            httpStatus = Response.Status.NOT_FOUND;
        } else if (BaseSecurityResource.USER_NOT_CONNECTED.equals(message)) {
            httpStatus = Response.Status.UNAUTHORIZED;
        } else if ("ERRORS.NOT_ALLOWED".equals(message)) {
            httpStatus = Response.Status.FORBIDDEN;
        } else {
            Log.error(ExceptionUtils.getStackTrace(exception));
        }

        return Response.status(httpStatus).entity(
            new ExceptionResponse(Instant.now(), message,
                ExceptionUtils.getStackTrace(exception)))
            .build();
    }
}
