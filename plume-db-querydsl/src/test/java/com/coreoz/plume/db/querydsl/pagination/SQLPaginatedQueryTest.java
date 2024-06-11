package com.coreoz.plume.db.querydsl.pagination;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.db.querydsl.DbQuerydslTestModule;
import com.coreoz.plume.db.querydsl.db.QUser;
import com.coreoz.plume.db.querydsl.db.User;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.querydsl.core.types.dsl.StringPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceTestRunner.class)
@GuiceModules(DbQuerydslTestModule.class)
public class SQLPaginatedQueryTest {

    @Inject
    TransactionManagerQuerydsl transactionManagerQuerydsl;

    @Before
    public void before() {
        User user = new User();
        user.setId(0L);
        user.setName("To fetch");
        this.transactionManagerQuerydsl.insert(QUser.user)
            .populate(user)
            .execute();
    }

    @Test
    public void should_paginate_users() {
        Page<User> page = SQLPaginatedQuery
            .fromQuery(
                this.transactionManagerQuerydsl.selectQuery()
                    .select(QUser.user)
                    .from(QUser.user)
            )
            .withSort(SortOption.from(UserSort.NAME, "DESC"))
            .paginate(10, 1);

        assertThat(page.getCurrentPage()).isNotZero();
        assertThat(page.getTotalElements()).isNotZero();
        assertThat(page.getElements()).isNotEmpty();
        assertThat(page.getTotalPages()).isNotZero();
        assertThat(page.isLastPage()).isTrue();
    }

    private enum UserSort implements SortPath {
        NAME(QUser.user.name)
        ;

        final StringPath sortColumn;

        public StringPath getSortColumn() {
            return this.sortColumn;
        }

        UserSort(StringPath sortColumn) {
            this.sortColumn = sortColumn;
        }
    }
}
