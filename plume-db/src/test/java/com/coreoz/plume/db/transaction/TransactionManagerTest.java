package com.coreoz.plume.db.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;

import com.zaxxer.hikari.HikariDataSource;

import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

public class TransactionManagerTest {

    private final HikariDataSource dataSource = new HikariDataSourceProvider(ConfigFactory.load()).get();

	@Test
	public void should_disable_autocommit_during_transaction() {
		TransactionManager transactionManager = new TransactionManager(dataSource);
		transactionManager.execute(connection -> {
			try {
				assertThat(connection.getAutoCommit()).isFalse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void should_leave_connection_with_autocommit() {
		TransactionManager transactionManager = new TransactionManager(dataSource);
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
