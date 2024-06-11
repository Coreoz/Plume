package com.coreoz.plume.db.querydsl.pagination;

import com.querydsl.core.types.Order;

import javax.annotation.Nullable;

public class SortOption {
    private final SortPath sortOn;
    private final Order sortDirection;

    private SortOption(
        SortPath sortOn,
        Order sortDirection
    ) {
        this.sortOn = sortOn;
        this.sortDirection = sortDirection;
    }

    public SortPath sortOn() {
        return sortOn;
    }

    public Order sortDirection() {
        return sortDirection;
    }

    public static SortOption from(@Nullable SortPath path, @Nullable String sortDirection) {
        if (path == null) {
            return null;
        }
        if (sortDirection == null) {
            return new SortOption(
                path,
                Order.ASC
            );
        }
        Order sortOrder = orderDirection(sortDirection.toUpperCase());
        return new SortOption(
            path,
            sortOrder
        );
    }

    private static Order orderDirection(@Nullable String sortDirection) {
        if (sortDirection == null) {
            return null;
        }
        try {
            return Order.valueOf(sortDirection);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
