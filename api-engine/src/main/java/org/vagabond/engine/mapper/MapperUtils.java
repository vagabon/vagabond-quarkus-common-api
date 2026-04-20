package org.vagabond.engine.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.vagabond.engine.crud.response.PageResponse;

public class MapperUtils {
    public static final ModelMapper mapper = new ModelMapper();

    private MapperUtils() {
    }

    public static <T, U> U toDto(T data, Class<U> dtoClass) {
        return data != null ? mapper.map(data, dtoClass) : null;
    }

    public static <T, U> List<U> toList(List<T> datas, Class<U> dtoClass) {
        return datas != null ? datas.stream().map(data -> mapper.map(data, dtoClass)).toList()
                : new ArrayList<>();
    }

    public static <T> PageResponse<T> toPage(PageResponse<?> response, Class<?> dtoClass) {
        var mapped = response.content().stream().map(item -> mapper.map(item, dtoClass)).toList();
        return (PageResponse<T>) new PageResponse<>(response.page(), response.totalPages(),
                response.totalElements(), response.max(), mapped);
    }
}
