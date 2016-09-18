package com.coreoz.plume.db.querydsl;

import org.junit.Ignore;

import com.coreoz.plume.conf.guice.GuiceConfModule;
import com.coreoz.plume.db.guice.DataSourceModule;
import com.coreoz.plume.db.guice.GuiceDbTestModule;
import com.google.inject.AbstractModule;

@Ignore
public class DbQuerydslTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new GuiceConfModule());
		install(new DataSourceModule());
		install(new GuiceDbTestModule());
	}

}
