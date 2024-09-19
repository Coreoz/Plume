package com.coreoz.plume.db.pagination;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a portion (or slice) of a larger dataset.
 *
 * @param items the items in the slice
 * @param hasMore boolean set to true if there is another slice after
 *
 * @param <T> The type of elements contained in the slice.
 */
public record Slice<T>(
    @Nonnull List<T> items,
    boolean hasMore
) implements Sliceable<T> {

    /**
     * Maps the items of the slice
     * @param mapper the mapper
     * @return the slice with mapped items
     * @param <U> The type of elements to be mapped to in the slice.
     * <br>
     * Usage example:
     * <code><pre>
     * public Page<UserUpdated> fetchUsers() {
     *   return this.userService.fetchSlice(1, 10).map(user -> new UserUpdated(user));
     * }
     * </pre></code>
     */
    public <U> Slice<U> map(@Nonnull Function<T, U> mapper) {
        return new Slice<>(
            this.items.stream().map(mapper).toList(),
            this.hasMore
        );
    }
}
