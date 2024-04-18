package de.keeeks.nucleo.core.api.utils.pagination;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PaginationResult<T>(List<T> list, int page, int totalPages) {
    public static <T> PaginationResult<T> create(List<T> fullList, int page, int pageSize) {
        List<T> subList = processListSplitting(fullList, page, pageSize);
        int totalPages = (int) Math.ceil((double) fullList.size() / pageSize);

        if (page > totalPages) {
            return new PaginationResult<>(
                    processListSplitting(fullList, totalPages, pageSize),
                    totalPages,
                    totalPages
            );
        }
        return new PaginationResult<>(
                subList,
                page,
                totalPages
        );
    }

    public static <T> PaginationResult<T> create(List<T> fullList, int page) {
        return create(
                fullList,
                page,
                10
        );
    }

    private static <T> @NotNull List<T> processListSplitting(List<T> fullList, int page, int pageSize) {
        List<T> subList;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(page * pageSize, fullList.size());
        subList = fullList.subList(
                fromIndex,
                toIndex
        );
        return subList;
    }
}