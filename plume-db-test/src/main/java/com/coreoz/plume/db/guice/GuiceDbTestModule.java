package com.coreoz.plume.db.guice;

import com.coreoz.plume.db.InitializeDatabase;
import com.google.inject.AbstractModule;

public class GuiceDbTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InitializeDatabase.class).asEagerSingleton();
	}

}
