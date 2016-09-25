package com.coreoz.plume.db.querydsl.crud;

import java.util.List;

import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;

public class QueryDslDao<T> {

	protected final TransactionManagerQuerydsl transactionManagerQuerydsl;
	protected final RelationalPath<T> table;

	private final OrderSpecifier<?> defaultOrder;

	public QueryDslDao(TransactionManagerQuerydsl transactionManagerQuerydsl, RelationalPath<T> table) {
		this(transactionManagerQuerydsl, table, null);
	}

	public QueryDslDao(TransactionManagerQuerydsl transactionManagerQuerydsl, RelationalPath<T> table,
			OrderSpecifier<?> defaultOrder) {
		this.transactionManagerQuerydsl = transactionManagerQuerydsl;
		this.table = table;
		this.defaultOrder = defaultOrder;
	}

	// API

	public List<T> findAll() {
		return selectFrom().fetch();
	}

	// dao API

	protected SQLQuery<T> selectFrom() {
		SQLQuery<T> query = transactionManagerQuerydsl
			.selectQuery()
			.select(table)
			.from(table);

		if(defaultOrder != null) {
			query.orderBy(defaultOrder);
		}

		return query;
	}

}
