package com.coreoz.plume.db;

import com.google.inject.AbstractModule;

public class DbTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InitializeDatabase.class).asEagerSingleton();
	}

}
