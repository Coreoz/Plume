package com.coreoz.plume.db.querydsl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.db.querydsl.generated.QUser;
import com.coreoz.plume.db.querydsl.mock.ConnectionMocked;
import com.coreoz.plume.db.querydsl.mock.DataSourceMocked;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.google.common.base.Throwables;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;

@RunWith(GuiceTestRunner.class)
@GuiceModules(DbQuerydslTestModule.class)
public class TransactionManagerQuerydslTest {

	@Inject
	private DataSource dataSource;

	@Test
	public void check_that_connection_is_released_after_select_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.selectQuery()
			.select(QUser.user)
			.from(QUser.user)
			.fetchFirst();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_select_transactional_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.selectQuery(testInstancesHolder.mockedConnection)
			.select(QUser.user)
			.from(QUser.user)
			.fetchFirst();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isFalse();
	}

	@Test
	public void check_that_connection_is_released_after_update_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.update(QUser.user)
			.where(QUser.user.id.eq(123456L))
			.set(QUser.user.active, true)
			.execute();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_update_transactional_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.update(QUser.user, testInstancesHolder.mockedConnection)
			.where(QUser.user.id.eq(123456L))
			.set(QUser.user.active, true)
			.execute();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isFalse();
	}

	@Test
	public void check_that_connection_is_released_after_delete_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.delete(QUser.user)
			.where(QUser.user.id.eq(123456L))
			.execute();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_delete_transactional_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.delete(QUser.user, testInstancesHolder.mockedConnection)
			.where(QUser.user.id.eq(123456L))
			.execute();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isFalse();
	}


	@Test
	public void check_that_connection_is_released_after_insert_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.insert(QUser.user)
			.columns(QUser.user.id)
			.values(65165465L)
			.execute();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_insert_transactional_query() throws SQLException {
		TestInstancesHolder testInstancesHolder = new TestInstancesHolder(dataSource);

		testInstancesHolder
			.transactionManager
			.insert(QUser.user, testInstancesHolder.mockedConnection)
			.columns(QUser.user.id)
			.values(132654741L)
			.execute();

		Assertions.assertThat(testInstancesHolder.mockedConnection.isCloseCalled()).isFalse();
	}

	private static class TestInstancesHolder {

		private DataSource mockedDataSource;
		private ConnectionMocked mockedConnection;
		private TransactionManagerQuerydsl transactionManager;

		public TestInstancesHolder(DataSource realDataSource) {
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

	}

}
