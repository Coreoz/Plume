package com.coreoz.plume.db.querydsl.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.coreoz.plume.db.transaction.TransactionManager;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLCloseListener;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.AbstractSQLClause;
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
		SQLQuery<Q> query = new SQLQuery<>(getConnectionProvider(), querydslConfiguration);
		query.addListener(SQLCloseListener.DEFAULT);
		return query;
	}

	public <Q> SQLQuery<Q> selectQuery(Connection connection) {
		return new SQLQuery<>(connection, querydslConfiguration);
	}

	public SQLDeleteClause delete(RelationalPath<?> path) {
		return autoCloseQuery(new SQLDeleteClause(getConnectionProvider(), querydslConfiguration, path));
	}

	public SQLDeleteClause delete(RelationalPath<?> path, Connection connection) {
		return new SQLDeleteClause(connection, querydslConfiguration, path);
	}

	public SQLInsertClause insert(RelationalPath<?> path) {
		return autoCloseQuery(new SQLInsertClause(getConnectionProvider(), querydslConfiguration, path));
	}

	public SQLInsertClause insert(RelationalPath<?> path, Connection connection) {
		return new SQLInsertClause(connection, querydslConfiguration, path);
	}

	public SQLUpdateClause update(RelationalPath<?> path) {
		return autoCloseQuery(new SQLUpdateClause(getConnectionProvider(), querydslConfiguration, path));
	}

	public SQLUpdateClause update(RelationalPath<?> path, Connection connection) {
		return new SQLUpdateClause(connection, querydslConfiguration, path);
	}

	// internal

	private <T extends AbstractSQLClause<?>> T autoCloseQuery(T query) {
		query.addListener(SQLCloseListener.DEFAULT);
		return query;
	}

	private Provider<Connection> getConnectionProvider() {
		return () -> {
			try {
				return dataSource().getConnection();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		};
	}

}
