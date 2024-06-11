package com.coreoz.plume.db.querydsl.pagination;

import lombok.Value;

import java.util.List;

@Value
public class Page<T> {
    List<T> elements;
    long totalElements;
    long pageCount;
    long currentPage;
    long currentPageSize;
    boolean lastPage;
}
