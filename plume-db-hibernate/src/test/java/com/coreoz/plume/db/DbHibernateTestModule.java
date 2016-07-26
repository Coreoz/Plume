package com.coreoz.plume.db;

import com.coreoz.plume.ConfModule;
import com.google.inject.AbstractModule;

public class DbHibernateTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfModule());
	}

}
