package com.coreoz.plume.db.querydsl.pagination;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.sql.SQLQuery;

public class SQLPaginatedQuery<U> {

    private final SQLQuery<U> sqlQuery;

    private SQLPaginatedQuery(SQLQuery<U> sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public static <U> SQLPaginatedQuery<U> fromQuery(SQLQuery<U> sqlQuery) {
        return new SQLPaginatedQuery<>(sqlQuery);
    }

    public SQLPaginatedQuery<U> withSort(SortOption sortOption) {
        return new SQLPaginatedQuery<>(
            sqlQuery
                .orderBy(
                    new OrderSpecifier<>(
                        sortOption.sortDirection(),
                        sortOption.sortOn().getSortColumn()
                    )
                )
        );
    }

    public Page<U> paginate(Pageable pageable) {
        return this.fetchPage(
            this.sqlQuery,
            pageable
        );
    }

    public Page<U> paginate(Integer size, Integer page) {
        return this.paginate(Pageable.ofPageSize(size).withPage(page));
    }

    private Page<U> fetchPage(SQLQuery<U> baseQuery, Pageable pageable) {
        QueryResults<U> paginatedQueryResults = applyPagination(baseQuery, pageable)
            .fetchResults();

        return new Page<>(
            paginatedQueryResults.getResults(),
            paginatedQueryResults.getTotal(),
            pageable.pageCount(paginatedQueryResults.getTotal()),
            pageable.getPage(),
            paginatedQueryResults.getResults().size(),
            pageable.isLastPage(paginatedQueryResults.getTotal())
        );
    }

    private static <U> SQLQuery<U> applyPagination(SQLQuery<U> query, Pageable pageable) {
        return query
            .offset(pageable.offset())
            .limit(pageable.limit());
    }
}
