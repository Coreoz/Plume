package com.coreoz.plume.db.pagination;

public class Pages {

    private Pages() {
        // hide implicit constructor
    }

    public static long offset(int pageNumber, int pageSize) {
        return (long) (pageNumber - 1) * pageSize;
    }

    public static long pageCount(int pageSize, long resultNumber) {
        return (resultNumber + pageSize - 1) / pageSize;
    }

    public static boolean hasMore(int pageNumber, int pageSize, long resultNumber) {
        return pageNumber < pageCount(pageSize, resultNumber);
    }
}
