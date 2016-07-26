package com.coreoz.plume.db;

import javax.sql.DataSource;

import org.junit.Ignore;

import com.coreoz.plume.ConfModule;
import com.coreoz.plume.db.DbTestModule;
import com.google.inject.AbstractModule;

@Ignore
public class DbHibernateOracleTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfModule());
		install(new DbTestModule());
		bind(DataSource.class).toProvider(DataSourceProvider.class);
	}

}
