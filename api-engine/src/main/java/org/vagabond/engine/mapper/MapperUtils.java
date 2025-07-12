package org.vagabond.engine.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.vagabond.engine.crud.response.PageResponse;

import io.smallrye.mutiny.Uni;

public class MapperUtils {
    public static final ModelMapper mapper = new ModelMapper();

    private MapperUtils() {
    }

    public static <T, U> U toDto(T data, Class<U> dtoClass) {
        return data != null ? mapper.map(data, dtoClass) : null;
    }

    public static <T, U> List<U> toList(List<T> datas, Class<U> dtoClass) {
        return datas != null ? datas.stream().map(data -> mapper.map(data, dtoClass)).toList() : null;
    }

    public static <T, U> PageResponse<U> toPage(PageResponse<T> response, Class<U> dtoClass) {
        return new PageResponse<U>(response.page(), response.totalPages(), response.totalElements(), response.max(),
                Uni.createFrom().item(MapperUtils.toList(response.content().await().indefinitely(), dtoClass)));
    }
}
