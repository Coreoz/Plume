package com.coreoz.plume.db.pagination;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a page of data in a paginated structure.
 *
 * @param items the items in the page
 * @param totalCount the total number of items in the collection
 * @param pagesCount the total number of pages
 * @param currentPage the current page, starts at 1
 * @param hasMore boolean set to true if there is another page after this one
 *
 * @param <T> The type of items contained in the page.
 */
public record Page<T>(
    @Nonnull List<T> items,
    long totalCount,
    long pagesCount,
    long currentPage,
    boolean hasMore
) implements Sliceable<T> {

    /**
     * Maps the items of the paginated list
     * @param mapper the mapper
     * @return the page with mapped items
     * @param <U> The type of elements to be mapped to in the page.
     * <br>
     * Usage example:
     * <code><pre>
     * public Page<UserUpdated> fetchUsers() {
     *   return this.userService.fetchPage(1, 10).map(user -> new UserUpdated(user));
     * }
     * </pre></code>
     */
    public <U> Page<U> map(@Nonnull Function<T, U> mapper) {
        return new Page<>(
            this.items.stream().map(mapper).toList(),
            this.totalCount,
            this.pagesCount,
            this.currentPage,
            this.hasMore
        );
    }

}
