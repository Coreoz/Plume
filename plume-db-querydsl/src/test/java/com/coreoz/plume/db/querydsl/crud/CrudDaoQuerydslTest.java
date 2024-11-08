package com.coreoz.plume.db.querydsl.crud;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.coreoz.plume.db.guice.GuiceTest;
import jakarta.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.coreoz.plume.db.querydsl.DbQuerydslTestModule;
import com.coreoz.plume.db.querydsl.db.QUser;
import com.coreoz.plume.db.querydsl.db.User;
import com.coreoz.plume.db.querydsl.db.UserDao;

@GuiceTest(DbQuerydslTestModule.class)
public class CrudDaoQuerydslTest {

	@Inject
	UserDao userDao;

	@Test
	public void should_list_users() {
		User user = new User();
		user.setName("To fetch");
		User insertedUser = userDao.save(user);

		List<User> users = userDao.findAll();

		assertThat(users).contains(insertedUser);
	}

	@Test
	public void should_find_user_by_id() {
		User user = new User();
		user.setName("To fetch by id");
		User insertedUser = userDao.save(user);

		User userDb = userDao.findById(insertedUser.getId());

		assertThat(userDb).isNotNull();
		assertThat(userDb.getName()).isEqualTo("To fetch by id");
	}

	@Test
	public void should_read_local_date() {
		LocalDateTime currentDate = LocalDateTime.now();
		Long userId = 198165421L;

		userDao
			.transactionManager
			.insert(QUser.user)
			.columns(QUser.user.id, QUser.user.creationDate)
			.values(userId, currentDate)
			.execute();

		User user = userDao.findById(userId);

		assertThat(user.getCreationDate()).isCloseTo(currentDate, Assertions.within(1, ChronoUnit.MILLIS));
	}

	@Test
	public void should_insert_user() {
		int nbUsersBeforeTest = userDao.findAll().size();

		User user = new User();
		user.setActive(true);
		user.setName("To insert");
		User insertedUser = userDao.save(user);

		List<User> users = userDao.findAll();

		assertThat(users.size()).isEqualTo(nbUsersBeforeTest + 1);
		assertThat(users).contains(insertedUser);
	}

	@Test
	public void should_update_user() {
		User user = new User();
		user.setActive(true);
		user.setName("To update");
		User insertedUser = userDao.save(user);

		insertedUser.setActive(false);
		userDao.save(insertedUser);

		User userDb = userDao.findById(insertedUser.getId());

		assertThat(userDb.getName()).isEqualTo("To update");
		assertThat(userDb.getActive()).isFalse();
	}

	@Test
	public void should_support_update_with_null_values() {
		User user = new User();
		user.setName("To update to null");
		User insertedUser = userDao.save(user);

		User userDb = userDao.findById(insertedUser.getId());
		assertThat(userDb.getName()).isEqualTo("To update to null");

		insertedUser.setName(null);
		userDao.save(insertedUser);

		userDb = userDao.findById(insertedUser.getId());
		assertThat(userDb.getName()).isNull();
	}

}
