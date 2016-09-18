package com.coreoz.plume.db.hibernate;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.sql2o.Sql2o;

import com.coreoz.plume.db.hibernate.datasource.DataSourceContainer;
import com.coreoz.plume.db.hibernate.datasource.HibernateFactory;
import com.coreoz.plume.db.hibernate.pagination.HibernateQueryDslPaginable;
import com.coreoz.plume.db.pagination.Paginable;
import com.google.common.base.Throwables;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import com.typesafe.config.Config;

@Singleton
public class TransactionManagerHibernate {

	private final DataSourceContainer dataSourceContainer;

	@Inject
	public TransactionManagerHibernate(Config config) {
		this(HibernateFactory.newContainer(config, "db"));
	}

	public TransactionManagerHibernate(DataSourceContainer dataSourceContainer) {
		this.dataSourceContainer = dataSourceContainer;
	}

	/**
	 * Permet d'executer un traitement sur la base de données par l'intermédiaire de l'entityManager d'hibernate
	 */
	public <T> T executeAndReturn(Function<EntityManager, T> toExecuteOnDb) {
		EntityManager entityManager = dataSourceContainer.getEntityManagerFactory().createEntityManager();
		try {
			entityManager.getTransaction().begin();
			T result = toExecuteOnDb.apply(entityManager);
			entityManager.getTransaction().commit();
			return result;
		} catch(Throwable e) {
			try {
				if(entityManager.getTransaction().isActive()) {
					entityManager.getTransaction().rollback();
				}
			} catch (Throwable e2) {
				// tant pis si on ne pas fermer la transaction
			}
			throw Throwables.propagate(e);
		} finally {
			entityManager.close();
		}
	}

	public void execute(Consumer<EntityManager> toExecuteOnDb) {
		executeAndReturn(em -> {
			toExecuteOnDb.accept(em);
			return null;
		});
	}

	// queryDSL helpers

	public HibernateQueryFactory queryDsl(EntityManager em) {
		return new HibernateQueryFactory(em.unwrap(Session.class));
	}

	public <T> T queryDslExecuteAndReturn(Function<HibernateQueryFactory, T> toExecuteOnDb) {
		return executeAndReturn(em -> toExecuteOnDb.apply(queryDsl(em)));
	}

	public void queryDslExecute(Consumer<HibernateQueryFactory> toExecuteOnDb) {
		queryDslExecuteAndReturn(query -> {
			toExecuteOnDb.accept(query);
			return null;
		});
	}

	public <T> Paginable<T> queryDslPaginate(Function<HibernateQueryFactory, HibernateQuery<T>> toExecuteOnDb) {
		return new HibernateQueryDslPaginable<>(this, toExecuteOnDb);
	}

	// sql2o

	public Sql2o sql2o() {
		return dataSourceContainer.getSql2o();
	}
	
	// datasource
	
	public DataSource dataSource() {
		return dataSourceContainer.getDataSource();
	}

}
