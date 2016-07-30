package com.coreoz.plume.db;

import javax.sql.DataSource;

import com.coreoz.plume.conf.guice.GuiceConfModule;
import org.junit.Ignore;

import com.coreoz.plume.db.guice.GuiceDbTestModule;
import com.google.inject.AbstractModule;

@Ignore
public class DbHibernateOracleTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new GuiceConfModule());
		install(new GuiceDbTestModule());
		bind(DataSource.class).toProvider(DataSourceProvider.class);
	}

}
