package com.coreoz.plume.db.hibernate.pagination;

import java.util.List;
import java.util.function.Function;

import com.coreoz.plume.db.hibernate.TransactionManagerHibernate;
import com.coreoz.plume.db.pagination.Paginable;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;

public class HibernateQueryDslPaginable<T> implements Paginable<T> {

	private final TransactionManagerHibernate dbExecutor;
	private final Function<HibernateQueryFactory, HibernateQuery<T>> baseQuery;

	public HibernateQueryDslPaginable(TransactionManagerHibernate dbExecutor, Function<HibernateQueryFactory, HibernateQuery<T>> baseQuery) {
		this.dbExecutor = dbExecutor;
		this.baseQuery = baseQuery;
	}

	@Override
	public long count() {
		return dbExecutor.queryDslExecuteAndReturn(query ->
			baseQuery
				.apply(query)
				.fetchCount()
		);
	}

	@Override
	public List<T> fetch() {
		return dbExecutor.queryDslExecuteAndReturn(query ->
			baseQuery
				.apply(query)
				.fetch()
		);
	}

	@Override
	public List<T> fetch(int page, int pageSize) {
		return dbExecutor.queryDslExecuteAndReturn(query ->
			baseQuery
				.apply(query)
				.offset(page * pageSize)
				.limit(pageSize)
				.fetch()
		);
	}

}
