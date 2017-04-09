package com.coreoz.plume.db.pagination;

import java.util.List;

/**
 * Describe an object that handles pagination.
 *
 * @param <E> The paginable element
 */
public interface Paginable<E> {

	/**
	 * Returns the number of elements available
	 */
	long count();

	/**
	 * Returns all the available elements list.
	 */
	List<E> fetch();

	/**
	 * Fetch a page of elements.
	 *
	 * @param page The page sought, starts at 0
	 * @param pageSize The number of elements by page
	 * @return the elements list on the page sought
	 * @throws IndexOutOfBoundsException If page &lt; 0 or if page*pageSize &gt; {@link #count()}
	 */
	List<E> fetch(int page, int pageSize);

}
