package com.coreoz.plume.db.querydsl.pagination;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PageableTest {

    @Test
    public void pageable_with_0_size_should_return_default() {
        Pageable pageable = Pageable.ofPageSize(0);

        assertThat(pageable.getSize()).isEqualTo(1);
        assertThat(pageable.getPage()).isEqualTo(1);
    }

    @Test
    public void pageable_with_0_limit_should_return_default() {
        Pageable pageable = Pageable.ofPageSize(0).withPage(0);

        assertThat(pageable.getPage()).isEqualTo(1);
    }

    @Test
    public void pageable_should_return_corresponding_offset() {
        Pageable pageable = Pageable.ofPageSize(10).withPage(2);

        assertThat(pageable.offset()).isEqualTo(10);
    }

    @Test
    public void pageable_with_page_1_should_return_corresponding_offset() {
        Pageable pageable = Pageable.ofPageSize(150).withPage(1);

        assertThat(pageable.offset()).isZero();
    }

    @Test
    public void pageable_should_return_corresponding_size() {
        Pageable pageable = Pageable.ofPageSize(150).withPage(1);

        assertThat(pageable.getSize()).isEqualTo(150);
    }

    @Test
    public void pageable_should_return_corresponding_page_count() {
        Pageable pageable = Pageable.ofPageSize(150);

        assertThat(pageable.pageCount(475)).isEqualTo(4);
    }

    @Test
    public void pageable_with_no_results_should_return_corresponding_page_count() {
        Pageable pageable = Pageable.ofPageSize(150);

        assertThat(pageable.pageCount(0)).isZero();
    }

    @Test
    public void pageable_should_return_corresponding_is_last_page() {
        Pageable pageable = Pageable.ofPageSize(150).withPage(3);

        assertThat(pageable.isLastPage(450)).isTrue();
    }

    @Test
    public void pageable_with_0_results_should_return_corresponding_is_last_page() {
        Pageable pageable = Pageable.ofPageSize(150).withPage(1);

        assertThat(pageable.isLastPage(0)).isFalse();
    }
}
