package com.coreoz.plume.db.querydsl.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.db.querydsl.DbQuerydslTestModule;
import com.coreoz.plume.db.querydsl.db.QUser;

@RunWith(GuiceTestRunner.class)
@GuiceModules(DbQuerydslTestModule.class)
public class TransactionManagerQuerydslTest {

	@Inject
	private DataSource dataSource;

	@Test
	public void check_that_connection_is_released_after_select_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.selectQuery()
			.select(QUser.user)
			.from(QUser.user)
			.fetchFirst();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_select_transactional_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.selectQuery(testInstancesHolder.getMockedConnection())
			.select(QUser.user)
			.from(QUser.user)
			.fetchFirst();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isFalse();
	}

	@Test
	public void check_that_connection_is_released_after_update_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.update(QUser.user)
			.where(QUser.user.id.eq(123456L))
			.set(QUser.user.active, true)
			.execute();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_update_transactional_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.update(QUser.user, testInstancesHolder.getMockedConnection())
			.where(QUser.user.id.eq(123456L))
			.set(QUser.user.active, true)
			.execute();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isFalse();
	}

	@Test
	public void check_that_connection_is_released_after_delete_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.delete(QUser.user)
			.where(QUser.user.id.eq(123456L))
			.execute();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_delete_transactional_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.delete(QUser.user, testInstancesHolder.getMockedConnection())
			.where(QUser.user.id.eq(123456L))
			.execute();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isFalse();
	}


	@Test
	public void check_that_connection_is_released_after_insert_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.insert(QUser.user)
			.columns(QUser.user.id)
			.values(65165465L)
			.execute();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_insert_transactional_query() throws SQLException {
		TransactionInstancesHolder testInstancesHolder = new TransactionInstancesHolder(dataSource);

		testInstancesHolder
			.getTransactionManager()
			.insert(QUser.user, testInstancesHolder.getMockedConnection())
			.columns(QUser.user.id)
			.values(132654741L)
			.execute();

		assertThat(testInstancesHolder.getMockedConnection().isCloseCalled()).isFalse();
	}

}
