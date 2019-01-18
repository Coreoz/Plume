package com.coreoz.plume.db;

import javax.inject.Inject;
import javax.inject.Singleton;
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
