package com.coreoz.plume.db;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

@Singleton
public class InitializeDatabase {

	@Inject
	public InitializeDatabase(DataSource dataSource) {
		Flyway
			.configure()
			.dataSource(dataSource)
			.outOfOrder(true)
			.load()
			.migrate();
	}

}
