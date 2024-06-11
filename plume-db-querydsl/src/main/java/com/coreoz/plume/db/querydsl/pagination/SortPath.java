package com.coreoz.plume.db.querydsl.pagination;

import com.querydsl.core.types.dsl.StringPath;

public interface SortPath {
    String name();
    StringPath getSortColumn();
}
