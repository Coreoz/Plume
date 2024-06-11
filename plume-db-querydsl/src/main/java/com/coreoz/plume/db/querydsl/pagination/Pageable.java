package com.coreoz.plume.db.querydsl.pagination;

public class Pageable {
    private static final int MIN_PAGE = 1;
    private static final int MIN_SIZE = 1;

    private final int page;
    private final int size;

    public Pageable(Integer page, Integer size) {
        this.page = Math.max(MIN_PAGE, page == null ? MIN_PAGE : page);
        this.size = Math.max(MIN_SIZE, size == null ? MIN_SIZE : size);
    }

    public static Pageable ofSize(int size) {
        return new Pageable(null, size);
    }

    public Pageable withPage(int page) {
        return new Pageable(page, this.size);
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long offset() {
        return (this.page - MIN_PAGE) * this.limit();
    }

    public long limit() {
        return this.size;
    }

    public long pageCount(long resultNumber) {
        return (resultNumber + this.size - 1) / this.size;
    }

    public boolean isLastPage(long resultNumber) {
        return pageCount(resultNumber) == this.page;
    }
}
