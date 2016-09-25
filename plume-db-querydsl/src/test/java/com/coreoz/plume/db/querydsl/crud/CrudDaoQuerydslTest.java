package com.coreoz.plume.db.querydsl.crud;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.coreoz.plume.db.querydsl.DbQuerydslTestModule;
import com.coreoz.plume.db.querydsl.dao.UserDao;
import com.coreoz.plume.db.querydsl.generated.User;

@RunWith(GuiceTestRunner.class)
@GuiceModules(DbQuerydslTestModule.class)
public class CrudDaoQuerydslTest {

	@Inject
	private UserDao userDao;

	@Test
	public void should_list_users() {
		List<User> users = userDao.findAll();

		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getName()).isEqualTo("User test");
	}

	@Test
	public void should_find_user_by_id() {
		User user = userDao.findById(1L);

		assertThat(user).isNotNull();
		assertThat(user.getName()).isEqualTo("User test");
		assertThat(user.getActive()).isTrue();
	}

	@Test
	public void should_read_local_date() {
		User user = userDao.findById(1L);

		assertThat(user.getCreationDate()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	public void should_insert_user() {
		User user = new User();
		user.setActive(true);
		user.setName("To insert");
		User insertedUser = userDao.save(user);

		List<User> users = userDao.findAll();

		assertThat(users.size()).isEqualTo(2);
		assertThat(users).contains(insertedUser);
	}

	@Test
	public void should_update_user() {
		User user = new User();
		user.setActive(false);
		user.setName("To update");
		user.setId(1L);
		userDao.save(user);

		List<User> users = userDao.findAll();

		assertThat(users.size()).isEqualTo(1);
		assertThat(users.get(0).getName()).isEqualTo("To update");
		assertThat(users.get(0).getActive()).isFalse();
	}

}
