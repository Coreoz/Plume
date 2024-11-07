package com.coreoz.plume.db.querydsl.crud;

import java.util.List;

import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import org.jetbrains.annotations.NotNull;

public class QueryDslDao<T> {

	protected final TransactionManagerQuerydsl transactionManager;
	protected final RelationalPath<T> table;

	private final OrderSpecifier<?> defaultOrder;

	public QueryDslDao(TransactionManagerQuerydsl transactionManager, RelationalPath<T> table) {
		this(transactionManager, table, null);
	}

	public QueryDslDao(TransactionManagerQuerydsl transactionManagerQuerydsl, RelationalPath<T> table,
			OrderSpecifier<?> defaultOrder) {
		this.transactionManager = transactionManagerQuerydsl;
		this.table = table;
		this.defaultOrder = defaultOrder;
	}

	// API

	@NotNull
    public List<T> findAll() {
		return selectFrom().fetch();
	}

	// dao API

	protected SQLQuery<T> selectFrom() {
		SQLQuery<T> query = transactionManager
			.selectQuery()
			.select(table)
			.from(table);

		if(defaultOrder != null) {
			query.orderBy(defaultOrder);
		}

		return query;
	}

}
