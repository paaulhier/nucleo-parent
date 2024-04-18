package de.keeeks.nucleo.core.api.utils.pagination;

import java.util.List;

public record PaginationResult<T>(List<T> list, int page, int totalPages) {

    public static <T> PaginationResult<T> create(List<T> fullList, int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(page * pageSize, fullList.size());
        return new PaginationResult<>(
                fullList.subList(fromIndex, toIndex),
                page,
                (int) Math.ceil((double) fullList.size() / pageSize)
        );
    }

    public static <T> PaginationResult<T> create(List<T> fullList, int page) {
        return create(
                fullList,
                page,
                10
        );
    }
}