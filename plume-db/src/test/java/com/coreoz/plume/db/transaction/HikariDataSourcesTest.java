package com.coreoz.plume.db.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import com.typesafe.config.ConfigFactory;

public class HikariDataSourcesTest {

	@Test
	public void should_create_data_source_from_config() throws SQLException {
		DataSource dataSource = HikariDataSources.fromConfig(ConfigFactory.load(), "db.hikari");
		assertThat(dataSource.getConnection().prepareStatement("SELECT 1").execute()).isTrue();
	}

}
