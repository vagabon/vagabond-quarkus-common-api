package org.vagabond.engine.crud.response;

import java.util.List;

import io.smallrye.mutiny.Uni;

public record PageResponse<T>(Integer page, Uni<Integer> totalPages, Uni<Long> totalElements, Integer max, Uni<List<T>> content) {
}
