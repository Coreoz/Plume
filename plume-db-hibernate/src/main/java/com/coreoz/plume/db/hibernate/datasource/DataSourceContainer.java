package com.coreoz.plume.db.hibernate.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.sql2o.Sql2o;

public class DataSourceContainer {

	private final EntityManagerFactory entityManagerFactory;
	private final Sql2o sql2o;
	private final DataSource dataSource;

	public DataSourceContainer(EntityManagerFactory entityManagerFactory, Sql2o sql2o) {
		this.entityManagerFactory = entityManagerFactory;
		this.sql2o = sql2o;
		this.dataSource = sql2o.getDataSource();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public Sql2o getSql2o() {
		return sql2o;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
