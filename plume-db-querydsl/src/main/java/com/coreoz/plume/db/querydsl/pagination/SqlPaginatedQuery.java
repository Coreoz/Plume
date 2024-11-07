package com.coreoz.plume.db.querydsl.pagination;

import com.coreoz.plume.db.pagination.Page;
import com.coreoz.plume.db.pagination.Pages;
import com.coreoz.plume.db.pagination.Slice;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.sql.SQLQuery;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Paginated query implementation with Querydsl
 * <br>
 * @param <U> The type of elements contained in the request.
 * <br>
 * Usage example:
 * <code><pre>
 * public Page<User> fetchUsers() {
 *   return SqlPaginatedQuery
 *             .fromQuery(
 *                 this.transactionManagerQuerydsl.selectQuery()
 *                     .select(QUser.user)
 *                     .from(QUser.user)
 *             )
 *             .withSort(QUser.user.name, Order.DESC)
 *             .fetchPage(1, 10);
 * }
 * </pre></code>
 */
public class SqlPaginatedQuery<U> {

    private final SQLQuery<U> sqlQuery;

    private SqlPaginatedQuery(SQLQuery<U> sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public static <U> SqlPaginatedQuery<U> fromQuery(SQLQuery<U> sqlQuery) {
        return new SqlPaginatedQuery<>(sqlQuery);
    }

    @SuppressWarnings("rawtypes")
    @Nonnull
    public <E extends Comparable> SqlPaginatedQuery<U> withSort(
        @Nonnull Expression<E> expression,
        @Nonnull Order sortDirection
    ) {
        return new SqlPaginatedQuery<>(
            sqlQuery
                .orderBy(
                    new OrderSpecifier<>(
                        sortDirection,
                        expression
                    )
                )
        );
    }

    /**
     * Fetches a page of the SQL query provided
     * @param pageNumber the number of the page queried (must be >= 1)
     * @param pageSize the size of the page queried (must be >= 1)
     * @return the corresponding page
     */
    @Nonnull
    public Page<U> fetchPage(
        int pageNumber,
        int pageSize
    ) {
        QueryResults<U> paginatedQueryResults = this.sqlQuery
            .offset(Pages.offset(pageNumber, pageSize))
            .limit(pageSize)
            .fetchResults();

        return new Page<>(
            paginatedQueryResults.getResults(),
            paginatedQueryResults.getTotal(),
            Pages.pageCount(pageSize, paginatedQueryResults.getTotal()),
            pageNumber,
            Pages.hasMore(pageNumber, pageSize, paginatedQueryResults.getTotal())
        );
    }

    /**
     * Fetches a slice of the SQL query provided
     * @param pageNumber the number of the page queried (must be >= 1)
     * @param pageSize the size of the page queried (must be >= 1)
     * @return the corresponding slice
     */
    @Nonnull
    public Slice<U> fetchSlice(
        int pageNumber,
        int pageSize
    ) {
        List<U> slicedQueryResults = this.sqlQuery
            .offset(Pages.offset(pageNumber, pageSize))
            .limit(pageSize + 1L)
            .fetch();

        boolean hasMore = slicedQueryResults.size() > pageSize;

        // Trim the results to the required size (if needed)
        List<U> items = hasMore ? slicedQueryResults.subList(0, pageSize) : slicedQueryResults;

        return new Slice<>(
            items,
            hasMore
        );
    }
}
