package com.coreoz.plume.db.querydsl.transaction;

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
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.function.Supplier;

import static com.pivovarit.function.ThrowingSupplier.unchecked;

@Singleton
public class TransactionManagerQuerydsl extends TransactionManager {

	private final Configuration querydslConfiguration;

	@Inject
	public TransactionManagerQuerydsl(DataSource dataSource, Config config) {
        super(dataSource);
            String dialect = config.getString("db.dialect");
        this.querydslConfiguration = new Configuration(QuerydslTemplates.valueOf(dialect).sqlTemplates());
	}

    public TransactionManagerQuerydsl(DataSource dataSource, Configuration querydslConfiguration) {
        super(dataSource);
        this.querydslConfiguration = querydslConfiguration;
    }

	// API

    @Nonnull
	public <Q> SQLQuery<Q> selectQuery() {
		SQLQuery<Q> query = new SQLQuery<>(getConnectionProvider(), querydslConfiguration);
		query.addListener(SQLCloseListener.DEFAULT);
		return query;
	}

    @Nonnull
	public <Q> SQLQuery<Q> selectQuery(@Nonnull Connection connection) {
		return new SQLQuery<>(connection, querydslConfiguration);
	}

    @Nonnull
	public SQLDeleteClause delete(@Nonnull RelationalPath<?> path) {
		return autoCloseQuery(new SQLDeleteClause(getConnectionProvider(), querydslConfiguration, path));
	}

    @Nonnull
	public SQLDeleteClause delete(@Nonnull RelationalPath<?> path, @Nonnull Connection connection) {
		return new SQLDeleteClause(connection, querydslConfiguration, path);
	}

    @Nonnull
	public SQLInsertClause insert(@Nonnull RelationalPath<?> path) {
		return autoCloseQuery(new SQLInsertClause(getConnectionProvider(), querydslConfiguration, path));
	}

    @Nonnull
	public SQLInsertClause insert(@Nonnull RelationalPath<?> path, @Nonnull Connection connection) {
		return new SQLInsertClause(connection, querydslConfiguration, path);
	}

    @Nonnull
	public SQLUpdateClause update(@Nonnull RelationalPath<?> path) {
		return autoCloseQuery(new SQLUpdateClause(getConnectionProvider(), querydslConfiguration, path));
	}

    @Nonnull
	public SQLUpdateClause update(@Nonnull RelationalPath<?> path, @Nonnull Connection connection) {
		return new SQLUpdateClause(connection, querydslConfiguration, path);
	}

	// internal

    @Nonnull
	private <T extends AbstractSQLClause<?>> T autoCloseQuery(@Nonnull T query) {
		query.addListener(SQLCloseListener.DEFAULT);
		return query;
	}

    @Nonnull
    private Supplier<Connection> getConnectionProvider() {
		return unchecked(dataSource()::getConnection);
	}
}
