package org.vagabond.engine.exeption.handler;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.exeption.dto.ExceptionResponse;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<RuntimeException> {

        @Override
        public Response toResponse(RuntimeException exception) {
                if (exception instanceof MetierException) {
                        Log.error(exception.getMessage());
                        StackTraceElement[] stackTraces = exception.getStackTrace();
                        StackTraceElement[] stackTraceElements = Arrays.stream(stackTraces).filter(
                                        trace -> trace.getClassName().contains("org.vagabond"))
                                        .toArray(StackTraceElement[]::new);
                        exception.setStackTrace(stackTraceElements);
                }
                Log.error(ExceptionUtils.getStackTrace(exception));
                return Response.status(Response.Status.BAD_REQUEST)
                                .entity(new ExceptionResponse(LocalDateTime.now(),
                                                exception.getMessage(),
                                                ExceptionUtils.getStackTrace(exception)))
                                .build();
        }
}
