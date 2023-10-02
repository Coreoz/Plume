package com.coreoz.plume.db.querydsl.crud;

import com.coreoz.plume.db.crud.CrudDao;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.coreoz.plume.db.utils.IdGenerator;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.DefaultMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;

public class CrudDaoQuerydsl<T extends CrudEntity> extends QueryDslDao<T> implements CrudDao<T> {

	private final NumberPath<Long> idPath;

	public CrudDaoQuerydsl(TransactionManagerQuerydsl transactionManager,
			RelationalPath<T> table) {
		this(transactionManager, table, null);
	}

	public CrudDaoQuerydsl(TransactionManagerQuerydsl transactionManager,
			RelationalPath<T> table, OrderSpecifier<?> defaultOrder) {
		this(transactionManager, table, defaultOrder, new IdPath(table));
	}

	public CrudDaoQuerydsl(TransactionManagerQuerydsl transactionManager,
			RelationalPath<T> table, OrderSpecifier<?> defaultOrder, NumberPath<Long> idPath) {
		super(transactionManager, table, defaultOrder);
		this.idPath = idPath;
	}

	// API

	@Override
	@Nullable
	public T findById(@Nonnull Long id) {
		return selectFrom()
			.where(idPath.eq(id))
			.fetchFirst();
	}

	@Override
	public T save(@Nonnull T entityToUpdate) {
		return transactionManager.executeAndReturn(connection ->
			save(entityToUpdate, connection)
		);
	}

	public T save(@Nonnull T entityToUpdate, @Nonnull Connection connection) {
		if(entityToUpdate.getId() == null) {
			// insert
			entityToUpdate.setId(generateIdentifier());
			transactionManager
				.insert(table, connection)
				.populate(entityToUpdate)
				.execute();
			return entityToUpdate;
		}
		// update
		transactionManager
			.update(table, connection)
			.populate(entityToUpdate, DefaultMapper.WITH_NULL_BINDINGS)
			.where(idPath.eq(entityToUpdate.getId()))
			.execute();
		return entityToUpdate;
	}

	@Override
	public long delete(@Nonnull Long id) {
		return transactionManager.executeAndReturn(connection ->
			delete(id, connection)
		);
	}

	public long delete(@Nonnull Long id, @Nonnull Connection connection) {
		return transactionManager
			.delete(table, connection)
			.where(idPath.eq(id))
			.execute();
	}

	// dao API

	protected long generateIdentifier() {
		return IdGenerator.generate();
	}

	// internal

	private static class IdPath extends NumberPath<Long> {
		private static final long serialVersionUID = -8749023770318917240L;

		IdPath(RelationalPath<?> base) {
			super(Long.class, base, "id");
		}
	}

}
