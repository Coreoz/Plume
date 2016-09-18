package com.coreoz.plume.db.guice;

import javax.sql.DataSource;

import com.coreoz.plume.db.transaction.DataSourceProvider;
import com.google.inject.AbstractModule;

public class DataSourceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataSource.class).toProvider(DataSourceProvider.class);
	}

}
