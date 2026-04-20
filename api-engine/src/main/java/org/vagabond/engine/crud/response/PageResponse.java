package org.vagabond.engine.crud.response;

import java.util.List;

public record PageResponse<T>(Integer page, Integer totalPages, Long totalElements, Integer max,
        List<T> content) {
}
