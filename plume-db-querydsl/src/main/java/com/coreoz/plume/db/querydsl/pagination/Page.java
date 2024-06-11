package com.coreoz.plume.db.querydsl.pagination;

import lombok.Value;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
public class Page<T> {
    List<T> elements;
    long totalElements;
    long totalPages;
    long currentPage;
    long currentPageSize;
    boolean lastPage;

    public <U> Page<U> mapElements(Function<T, U> mapper) {
        return new Page<>(
            this.elements.stream().map(mapper).collect(Collectors.toList()),
            this.totalElements,
            this.totalPages,
            this.currentPage,
            this.currentPageSize,
            this.lastPage
        );
    }
}
