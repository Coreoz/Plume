package com.coreoz.plume.db.querydsl.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.coreoz.plume.db.crud.CrudDao;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.coreoz.plume.db.utils.IdGenerator;
import com.google.common.base.Throwables;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;

public class CrudDaoQuerydsl<T extends CrudEntity> implements CrudDao<T> {

	protected final TransactionManagerQuerydsl transactionManagerQuerydsl;
	protected final RelationalPath<T> table;

	private final OrderSpecifier<?> defaultOrder;
	private final IdPath idPath;

	public CrudDaoQuerydsl(TransactionManagerQuerydsl transactionManagerQuerydsl, RelationalPath<T> table) {
		this(transactionManagerQuerydsl, table, null);
	}

	public CrudDaoQuerydsl(TransactionManagerQuerydsl transactionManagerQuerydsl, RelationalPath<T> table,
			OrderSpecifier<?> defaultOrder) {
		this(transactionManagerQuerydsl, table, defaultOrder, new IdPath(table));
	}

	public CrudDaoQuerydsl(TransactionManagerQuerydsl transactionManagerQuerydsl, RelationalPath<T> table,
			OrderSpecifier<?> defaultOrder, IdPath idPath) {
		this.transactionManagerQuerydsl = transactionManagerQuerydsl;
		this.table = table;
		this.defaultOrder = defaultOrder;
		this.idPath = idPath;
	}

	// API

	@Override
	public List<T> findAll() {
		return selectFrom().fetch();
	}

	@Override
	public T findById(Long id) {
		return selectFrom()
			.where(idPath.eq(id))
			.fetchFirst();
	}

	@Override
	public T save(T entityToUpdate) {
		try {
			return save(entityToUpdate, transactionManagerQuerydsl.dataSource().getConnection());
		} catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}

	public T save(T entityToUpdate, Connection connection) {
		if(entityToUpdate.getId() == null) {
			// insert
			entityToUpdate.setId(IdGenerator.generate());
			transactionManagerQuerydsl
				.insert(table, connection)
				.populate(entityToUpdate)
				.execute();
			return entityToUpdate;
		}
		// update
		transactionManagerQuerydsl
			.update(table, connection)
			.populate(entityToUpdate)
			.execute();
		return entityToUpdate;
	}

	@Override
	public long delete(Long id) {
		try {
			return delete(id, transactionManagerQuerydsl.dataSource().getConnection());
		} catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}

	public long delete(Long id, Connection connection) {
		return transactionManagerQuerydsl
			.delete(table, connection)
			.where(idPath.eq(id))
			.execute();
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

	// internal

	private static class IdPath extends NumberPath<Long> {
		private static final long serialVersionUID = -8749023770318917240L;

		IdPath(RelationalPath<?> base) {
			super(Long.class, base, "id");
		}
	}

}
