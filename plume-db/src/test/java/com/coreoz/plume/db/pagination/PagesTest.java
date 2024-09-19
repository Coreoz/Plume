package com.coreoz.plume.db.pagination;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PagesTest {

    @Test
    public void pageable_should_return_corresponding_offset() {
        assertThat(Pages.offset(2, 10)).isEqualTo(10);
    }

    @Test
    public void pageable_with_page_1_should_return_corresponding_offset() {
        assertThat(Pages.offset(1, 150)).isZero();
    }

    @Test
    public void pageable_should_return_corresponding_page_count() {
        assertThat(Pages.pageCount(150, 475)).isEqualTo(4);
    }

    @Test
    public void pageable_with_no_results_should_return_corresponding_page_count() {
        assertThat(Pages.pageCount(150, 0)).isZero();
    }

    @Test
    public void pageable_should_return_corresponding_has_more() {
        assertThat(Pages.hasMore(3, 150, 450)).isFalse();
    }

    @Test
    public void pageable_with_0_results_should_return_corresponding_has_more() {
        assertThat(Pages.hasMore(1, 150, 0)).isFalse();
    }
}
