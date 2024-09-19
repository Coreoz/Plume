package com.coreoz.plume.db.transaction;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import javax.sql.DataSource;

/**
 * Expose a {@link DataSource} Object through dependency injection.
 */
@Singleton
public class DataSourceProvider implements Provider<DataSource> {

	private final DataSource dataSource;

	@Inject
	public DataSourceProvider(TransactionManager transactionManager) {
		this.dataSource = transactionManager.dataSource();
	}

	@Override
	public DataSource get() {
		return dataSource;
	}

}
