package org.vagabond.engine.exeption.handler;

import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.exeption.dto.ExceptionResponse;

import io.quarkus.logging.Log;

@Provider
public class ExceptionHandler implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        var message = exception.getMessage();
        if (exception instanceof MetierException || exception instanceof NotFoundException) {
            StackTraceElement[] stackTraces = exception.getStackTrace();
            StackTraceElement[] stackTraceElements = Arrays.stream(stackTraces)
                    .filter(trace -> trace.getClassName().contains("org.vagabond")).toArray(StackTraceElement[]::new);
            var stack = new StackTraceElement[] {};
            if (stackTraceElements.length > 0) {
                stack = new StackTraceElement[] { stackTraceElements[0] };
            }
            exception.setStackTrace(stack);
        }
        if (exception instanceof ConstraintViolationException) {
            message = "ERRORS.CONTRAINTS_VIOLATION";
        }
        Log.error(ExceptionUtils.getStackTrace(exception));
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionResponse(LocalDateTime.now(), message, ExceptionUtils.getStackTrace(exception))).build();
    }
}
