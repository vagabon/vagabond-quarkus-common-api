package org.vagabond.engine.crud.dto;

import java.util.List;

public record PageResponse(Integer page, Integer totalPages, Long totalElements, Integer max, List<?> content) {
}
