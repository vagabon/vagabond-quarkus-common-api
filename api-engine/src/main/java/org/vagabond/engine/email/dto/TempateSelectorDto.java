package org.vagabond.engine.email.dto;

import jakarta.validation.constraints.NotBlank;

public record TempateSelectorDto(@NotBlank String placeholder, @NotBlank String value) {
}
