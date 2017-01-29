package com.coreoz.plume.db.querydsl.crud;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.db.querydsl.DbQuerydslTestModule;
import com.coreoz.plume.db.querydsl.db.User;
import com.coreoz.plume.db.querydsl.db.UserDao;
import com.coreoz.plume.db.querydsl.transaction.TransactionInstancesHolder;

/**
 * Ensure that transaction management behave as expected for daos
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(DbQuerydslTestModule.class)
public class CrudDaoQuerydslTransactionTest {

	@Inject
	DataSource dataSource;

	@Test
	public void check_that_connection_is_released_after_findAll() throws SQLException {
		DaoInstancesHolder daoInstancesHolder = new DaoInstancesHolder(dataSource);

		daoInstancesHolder.userDao.findAll();

		assertThat(daoInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_released_after_findById() throws SQLException {
		DaoInstancesHolder daoInstancesHolder = new DaoInstancesHolder(dataSource);

		daoInstancesHolder.userDao.findById(1L);

		assertThat(daoInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_released_after_save() throws SQLException {
		DaoInstancesHolder daoInstancesHolder = new DaoInstancesHolder(dataSource);

		User user = new User();
		user.setName("test");
		daoInstancesHolder.userDao.save(user);

		assertThat(daoInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_transactional_save() throws SQLException {
		DaoInstancesHolder daoInstancesHolder = new DaoInstancesHolder(dataSource);

		User user = new User();
		user.setName("test");
		daoInstancesHolder.userDao.save(user, daoInstancesHolder.getMockedConnection());

		assertThat(daoInstancesHolder.getMockedConnection().isCloseCalled()).isFalse();
	}

	@Test
	public void check_that_connection_is_released_after_delete() throws SQLException {
		DaoInstancesHolder daoInstancesHolder = new DaoInstancesHolder(dataSource);

		daoInstancesHolder.userDao.delete(123132464L);

		assertThat(daoInstancesHolder.getMockedConnection().isCloseCalled()).isTrue();
	}

	@Test
	public void check_that_connection_is_NOT_released_after_transactional_delete() throws SQLException {
		DaoInstancesHolder daoInstancesHolder = new DaoInstancesHolder(dataSource);

		daoInstancesHolder.userDao.delete(123132464L, daoInstancesHolder.getMockedConnection());

		assertThat(daoInstancesHolder.getMockedConnection().isCloseCalled()).isFalse();
	}

	private static class DaoInstancesHolder extends TransactionInstancesHolder {
		private UserDao userDao;

		public DaoInstancesHolder(DataSource realDataSource) {
			super(realDataSource);
			userDao = new UserDao(getTransactionManager());
		}
	}

}
