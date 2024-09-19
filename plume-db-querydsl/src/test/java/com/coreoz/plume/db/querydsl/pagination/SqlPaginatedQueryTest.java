package com.coreoz.plume.db.querydsl.pagination;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.db.pagination.Page;
import com.coreoz.plume.db.pagination.Slice;
import com.coreoz.plume.db.querydsl.DbQuerydslTestModule;
import com.coreoz.plume.db.querydsl.db.QUser;
import com.coreoz.plume.db.querydsl.db.User;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.querydsl.core.types.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * file V2__add_users.sql
 * 10 users were added
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(DbQuerydslTestModule.class)
public class SqlPaginatedQueryTest {

    @Inject
    TransactionManagerQuerydsl transactionManagerQuerydsl;

    @Test
    public void fetch_page_with_correct_pagination_should_paginate_users() {
        Page<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.DESC)
            .fetchPage(1, 10);

        assertThat(page.pagesCount()).isEqualTo(1);
        assertThat(page.totalCount()).isEqualTo(10);
        assertThat(page.items()).hasSize(10);
        assertThat(page.hasMore()).isFalse();
    }

    @Test
    public void fetch_page_with_wrong_pagination_should_return_empty_items() {
        Page<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.DESC)
            .fetchPage(2, 10);

        assertThat(page.pagesCount()).isEqualTo(1);
        assertThat(page.totalCount()).isEqualTo(10);
        assertThat(page.items()).isEmpty();
        assertThat(page.hasMore()).isFalse();
    }

    @Test
    public void fetch_page_with_minimum_page_and_page_size_should_return_results() {
        Page<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchPage(1, 1); // Minimum page number and page size

        assertThat(page.pagesCount()).isEqualTo(10);
        assertThat(page.totalCount()).isEqualTo(10);
        assertThat(page.items()).hasSize(1); // Only one item expected due to page size of 1
        assertThat(page.hasMore()).isTrue(); // Has more items since page size is small
    }

    @Test
    public void fetch_page_with_page_size_larger_than_total_results_should_return_all() {
        Page<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchPage(1, 100); // Large page size compared to available results

        assertThat(page.totalCount()).isEqualTo(10);
        assertThat(page.items()).hasSize(10); // Should return all available users
        assertThat(page.hasMore()).isFalse(); // No more items because page size exceeds total results
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetch_page_with_invalid_negative_page_number_should_throw_exception() {
        SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchPage(-1, 10); // Invalid negative page number
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetch_page_with_invalid_negative_page_size_should_throw_exception() {
        SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchPage(1, -10); // Invalid negative page size
    }

    @Test
    public void fetch_page_without_sorting_should_paginate_users() {
        Page<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .fetchPage(1, 10); // No sorting applied

        assertThat(page.pagesCount()).isEqualTo(1);
        assertThat(page.totalCount()).isEqualTo(10);
        assertThat(page.items()).hasSize(10);
        assertThat(page.hasMore()).isFalse(); // Assuming there aren't more than 10 users in the test
    }

    @Test
    public void fetch_page_with_empty_query_should_return_no_results() {
        Page<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
                    .where(QUser.user.name.eq("Non-existent user")) // Query with no matches
            )
            .fetchPage(1, 10);

        assertThat(page.totalCount()).isEqualTo(0); // No total count
        assertThat(page.items()).isEmpty(); // No items should be returned
        assertThat(page.hasMore()).isFalse(); // No more pages since there's no data
    }

    @Test
    public void fetch_slice_with_minimum_page_and_page_size_should_return_results() {
        Slice<User> slice = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchSlice(1, 1); // Minimum page number and page size

        assertThat(slice.items()).hasSize(1); // Only one item expected due to page size of 1
        assertThat(slice.hasMore()).isTrue(); // Has more items because of page size being small
    }

    @Test
    public void fetch_slice_with_empty_query_should_return_no_results() {
        Slice<User> slice = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
                    .where(QUser.user.name.eq("Non-existent user")) // Query with no matches
            )
            .fetchSlice(1, 10);

        assertThat(slice.items()).isEmpty(); // No items should be returned
        assertThat(slice.hasMore()).isFalse(); // No more items since there's no data
    }

    @Test
    public void fetch_slice_with_correct_pagination_should_slice_users() {
        Slice<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchSlice(1, 50);

        assertThat(page.items()).isNotEmpty();
        assertThat(page.hasMore()).isFalse();
    }

    @Test
    public void fetch_slice_with_wrong_pagination_should_return_empty_items() {
        Slice<User> page = SqlPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(QUser.user.name, Order.ASC)
            .fetchSlice(2, 50);

        assertThat(page.items()).isEmpty();
        assertThat(page.hasMore()).isFalse();
    }
}
