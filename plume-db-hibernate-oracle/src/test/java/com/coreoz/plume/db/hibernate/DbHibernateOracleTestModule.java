package com.coreoz.plume.db.hibernate;

import com.coreoz.plume.conf.guice.GuiceConfModule;
import com.coreoz.plume.db.guice.GuiceDbTestModule;
import com.google.inject.AbstractModule;
import org.junit.Ignore;

import javax.sql.DataSource;

@Ignore
public class DbHibernateOracleTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new GuiceConfModule());
		install(new GuiceDbTestModule());
		bind(DataSource.class).toProvider(DataSourceProvider.class);
	}

}
