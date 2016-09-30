package com.coreoz.plume.db.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.typesafe.config.ConfigFactory;

public class TransactionManagerTest {

	@Test
	public void should_disable_autocommit_during_transaction() throws SQLException {
		TransactionManager transactionManager = new TransactionManager(ConfigFactory.load());
		transactionManager.execute(connection -> {
			try {
				assertThat(connection.getAutoCommit()).isFalse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void should_leave_connection_with_autocommit() throws SQLException {
		TransactionManager transactionManager = new TransactionManager(ConfigFactory.load());
		transactionManager.execute(connection -> {
			try {
				connection.prepareStatement("SELECT 1").execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		try(Connection connection = transactionManager.dataSource().getConnection()){
			assertThat(connection.getAutoCommit()).isTrue();
		} catch (Exception e2) {
		}
	}

}
