package com.coreoz.plume.db.querydsl.guice;

import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.coreoz.plume.db.transaction.TransactionManager;
import com.google.inject.AbstractModule;

public class GuiceQuerydslModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TransactionManager.class).to(TransactionManagerQuerydsl.class);
	}

}
