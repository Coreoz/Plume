package com.coreoz.plume.db.querydsl.guice;

import com.coreoz.plume.db.guice.DataSourceModule;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.coreoz.plume.db.transaction.TransactionManager;
import com.google.inject.AbstractModule;

public class GuiceQuerydslModule extends AbstractModule {

	@Override
	protected void configure() {
        install(new DataSourceModule());
		bind(TransactionManager.class).to(TransactionManagerQuerydsl.class);
	}
}
