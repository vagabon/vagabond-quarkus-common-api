package org.vagabond.engine.exeption.dto;

import java.time.LocalDateTime;

public record ExceptionResponse(LocalDateTime timestamp, String debugMessage,
                String message) {
}
