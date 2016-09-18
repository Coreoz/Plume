package com.coreoz.plume.db.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import javax.sql.DataSource;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;

public class TransactionManager {

	private final DataSource dataSource;

	public TransactionManager(Config config) {
		this(config, "db");
	}

	public TransactionManager(Config config, String prefix) {
		this(HikariDataSources.fromConfig(config, prefix));
	}

	public TransactionManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public <T> T executeAndReturn(Function<Connection, T> toExecuteOnDb) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
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
			throw Throwables.propagate(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// never mind if the connection cannot be closed
				}
			}
		}
	}

}
