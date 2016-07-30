package com.coreoz.plume.db;

import com.coreoz.plume.conf.guice.GuiceConfModule;
import com.google.inject.AbstractModule;

public class DbHibernateTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new GuiceConfModule());
	}

}
