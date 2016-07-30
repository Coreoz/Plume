package com.coreoz.plume.db.crud;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;

import com.coreoz.plume.db.TransactionManager;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.hibernate.HibernateQuery;

public class CrudDao<T> {

	private final EntityPathBase<T> queryDslEntity;
	private final IdPath idPath;
	protected final TransactionManager transactionManager;

	private final OrderSpecifier<?> defaultOrder;

	public CrudDao(EntityPathBase<T> queryDslEntity, TransactionManager transactionManager) {
		this(queryDslEntity, transactionManager, null);
	}

	public CrudDao(EntityPathBase<T> queryDslEntity, TransactionManager transactionManager, OrderSpecifier<?> defaultOrder) {
		this.queryDslEntity = queryDslEntity;
		this.transactionManager = transactionManager;
		this.defaultOrder = defaultOrder;

		this.idPath = new IdPath(queryDslEntity);
	}

	// API

	public List<T> fetchAll() {
		return search();
	}

	public T findById(Long id) {
		return findOne(idPath.eq(id));
	}

	public T save(T entityToUpdate) {
		return transactionManager.executeAndReturn(em -> save(entityToUpdate, em));
	}

	public T save(T entityToUpdate, EntityManager em) {
		return em.merge(entityToUpdate);
	}

	public void delete(Long id) {
		transactionManager.execute(em -> delete(id, em));
	}

	public void delete(Long id, EntityManager em) {
		transactionManager
			.queryDsl(em)
			.delete(queryDslEntity)
			.where(idPath.eq(id))
			.execute();
	}

	// protected

	protected long count(Predicate... predicates) {
		return withPredicate(HibernateQuery::fetchCount, predicates);
	}

	protected List<T> search(Predicate... predicates) {
		return withPredicate(
			fetchQuery -> {
				if(defaultOrder != null) {
					fetchQuery.orderBy(defaultOrder);
				}

				return fetchQuery.fetch();
			},
			predicates
		);
	}

	protected T findOne(Predicate... predicates) {
		return withPredicate(HibernateQuery::fetchOne, predicates);
	}

	protected<R> R withPredicate(Function<HibernateQuery<T>, R> toResult, Predicate... predicates) {
		return transactionManager.queryDslExecuteAndReturn(query -> {
			HibernateQuery<T> fetchQuery = query.selectFrom(queryDslEntity);

			if(predicates != null && predicates.length > 0) {
				fetchQuery.where(predicates);
			}

			return toResult.apply(fetchQuery);
		});
	}

	// internal

	private static class IdPath extends NumberPath<Long> {
		private static final long serialVersionUID = -8749023770318917240L;

		IdPath(EntityPathBase<?> base) {
			super(Long.class, base, "id");
		}
	}

}
