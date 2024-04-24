package de.keeeks.nucleo.core.api.utils.pagination;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public record PaginationResult<T>(
        List<T> list,
        int totalAmount,
        int page,
        int totalPages
) implements Iterable<T> {
    public static <T> PaginationResult<T> create(List<T> fullList, int page, int pageSize) {
        List<T> subList = processListSplitting(fullList, page, pageSize);
        int totalPages = (int) Math.ceil((double) fullList.size() / pageSize);

        if (page > totalPages) {
            return new PaginationResult<>(
                    processListSplitting(fullList, totalPages, pageSize),
                    fullList.size(),
                    totalPages,
                    totalPages
            );
        }
        return new PaginationResult<>(
                subList,
                fullList.size(),
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
        int fromIndex = (page - 1) * pageSize;
        return fullList.stream().skip(
                fromIndex
        ).limit(pageSize).toList();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}