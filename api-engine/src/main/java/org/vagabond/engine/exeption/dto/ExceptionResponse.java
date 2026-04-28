package org.vagabond.engine.exeption.dto;

import java.time.Instant;

public record ExceptionResponse(Instant timestamp, String debugMessage, String message) {
}
