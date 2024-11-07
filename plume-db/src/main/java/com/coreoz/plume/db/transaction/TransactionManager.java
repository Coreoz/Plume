package com.coreoz.plume.db.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javax.sql.DataSource;

import com.google.common.base.Throwables;

/**
 * Handle transactions over a classic JDBC {@link Connection}.
 */
@Singleton
public class TransactionManager {

	private final DataSource dataSource;

    @Inject
    public TransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	// API

	public DataSource dataSource() {
		return dataSource;
	}

	public <T> T executeAndReturn(@Nonnull Function<Connection, T> toExecuteOnDb) {
		Connection connection = null;
		Boolean initialAutoCommit = null;
		try {
			connection = dataSource().getConnection();
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
				// if the rollback failed, raise an exception about the rollback failure
				// and the original error
				RuntimeException combinedException = new RuntimeException(e2);
				combinedException.addSuppressed(e);
				throw combinedException;
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

	public void execute(@Nonnull Consumer<Connection> toExecuteOnDb) {
		executeAndReturn(connection -> {
			toExecuteOnDb.accept(connection);
			return null;
		});
	}

}
