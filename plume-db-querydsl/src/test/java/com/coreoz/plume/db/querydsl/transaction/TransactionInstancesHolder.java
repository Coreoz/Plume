package com.coreoz.plume.db.querydsl.transaction;

import java.sql.Connection;

import javax.sql.DataSource;

import com.coreoz.plume.db.querydsl.mock.ConnectionMocked;
import com.coreoz.plume.db.querydsl.mock.DataSourceMocked;
import com.google.common.base.Throwables;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;

public class TransactionInstancesHolder {

	private DataSource mockedDataSource;
	private ConnectionMocked mockedConnection;
	private TransactionManagerQuerydsl transactionManager;

	public TransactionInstancesHolder(DataSource realDataSource) {
		try {
			Connection realConnection = realDataSource.getConnection();
			mockedConnection = new ConnectionMocked(realConnection);
			mockedDataSource = new DataSourceMocked(mockedConnection);

			transactionManager = new TransactionManagerQuerydsl(
				mockedDataSource,
				new Configuration(H2Templates.DEFAULT)
			);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public DataSource getMockedDataSource() {
		return mockedDataSource;
	}

	public ConnectionMocked getMockedConnection() {
		return mockedConnection;
	}

	public TransactionManagerQuerydsl getTransactionManager() {
		return transactionManager;
	}

}