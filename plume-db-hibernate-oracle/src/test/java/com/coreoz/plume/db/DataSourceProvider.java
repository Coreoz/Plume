package com.coreoz.plume.db;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.junit.Ignore;

import com.coreoz.plume.db.TransactionManagerOracle;

@Ignore
@Singleton
public class DataSourceProvider implements Provider<DataSource> {

	private final TransactionManagerOracle transactionManager;
	
	@Inject
	public DataSourceProvider(TransactionManagerOracle transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public DataSource get() {
		return transactionManager.dataSource();
	}

}
