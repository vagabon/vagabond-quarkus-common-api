package org.vagabond.engine.email.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record ResponseBodyDto(@NotBlank String content, List<String> errors) {
}
