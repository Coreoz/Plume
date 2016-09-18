package com.coreoz.plume.db.querydsl.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.coreoz.plume.db.transaction.TransactionManager;
import com.google.common.base.Throwables;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.typesafe.config.Config;

@Singleton
public class TransactionManagerQuerydsl extends TransactionManager {

	private final Configuration querydslConfiguration;

	@Inject
	public TransactionManagerQuerydsl(Config config) {
		this(config, "db");
	}

	public TransactionManagerQuerydsl(Config config, String prefix) {
		super(config, prefix);

		String dialect = config.getString(prefix + ".dialect");
		this.querydslConfiguration = new Configuration(QuerydslTemplates.valueOf(dialect).sqlTemplates());
	}

	public TransactionManagerQuerydsl(DataSource dataSource, Configuration querydslConfiguration) {
		super(dataSource);

		this.querydslConfiguration = querydslConfiguration;
	}

	// API

	public <Q> SQLQuery<Q> selectQuery() {
		try {
			return selectQuery(dataSource().getConnection());
		} catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}

	public <Q> SQLQuery<Q> selectQuery(Connection connection) {
		return new SQLQuery<Q>(connection, querydslConfiguration);
	}

	public SQLDeleteClause delete(RelationalPath<?> path) {
		try {
			return delete(path, dataSource().getConnection());
		} catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}

	public SQLDeleteClause delete(RelationalPath<?> path, Connection connection) {
		return new SQLDeleteClause(connection, querydslConfiguration, path);
	}

	public SQLInsertClause insert(RelationalPath<?> path) {
		try {
			return insert(path, dataSource().getConnection());
		} catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}

	public SQLInsertClause insert(RelationalPath<?> path, Connection connection) {
		return new SQLInsertClause(connection, querydslConfiguration, path);
	}

	public SQLUpdateClause update(RelationalPath<?> path) {
		try {
			return update(path, dataSource().getConnection());
		} catch (SQLException e) {
			throw Throwables.propagate(e);
		}
	}

	public SQLUpdateClause update(RelationalPath<?> path, Connection connection) {
		return new SQLUpdateClause(connection, querydslConfiguration, path);
	}

}
