package com.coreoz.plume.db.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;

/**
 * Handle transactions over a classic JDBC {@link Connection}.
 */
@Singleton
public class TransactionManager {

	private final DataSource dataSource;

	@Inject
	public TransactionManager(Config config) {
		this(config, "db");
	}

	public TransactionManager(Config config, String prefix) {
		this(HikariDataSources.fromConfig(config, prefix + ".hikari"));
	}

	public TransactionManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// API

	public DataSource dataSource() {
		return dataSource;
	}

	public <T> T executeAndReturn(Function<Connection, T> toExecuteOnDb) {
		Connection connection = null;
		Boolean initialAutoCommit = null;
		try {
			connection = dataSource.getConnection();
			initialAutoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			T result = toExecuteOnDb.apply(connection);
			connection.commit();
			return result;
		} catch(Throwable e) {
			try {
				if(connection != null) {
					connection.rollback();
				}
			} catch (Throwable e2) {
				// never mind if the connection cannot be rolled back
			}
			Throwables.throwIfUnchecked(e);
			throw new RuntimeException(e);
		} finally {
			if(connection != null) {
				try {
					if(initialAutoCommit != null) {
						connection.setAutoCommit(initialAutoCommit);
					}
					connection.close();
				} catch (SQLException e) {
					// never mind if the connection cannot be closed
				}
			}
		}
	}

	public void execute(Consumer<Connection> toExecuteOnDb) {
		executeAndReturn(connection -> {
			toExecuteOnDb.accept(connection);
			return null;
		});
	}

}
