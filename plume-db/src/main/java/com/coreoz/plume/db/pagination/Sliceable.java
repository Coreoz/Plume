package com.coreoz.plume.db.pagination;

import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Common interface for {@link Page} or {@link Slice} data structures.
 *
 * @param <T> The type of elements contained in the pageable structure.
 */
public interface Sliceable<T> {
    /**
     * Returns the list of elements contained in this sliceable structure.
     *
     * @return A list of elements.
     */
    @Nonnull
    List<T> items();

    /**
     * Indicates whether there are more elements available beyond this sliceable structure.
     *
     * @return {@code true} if there are more elements, {@code false} otherwise.
     */
    boolean hasMore();
}
