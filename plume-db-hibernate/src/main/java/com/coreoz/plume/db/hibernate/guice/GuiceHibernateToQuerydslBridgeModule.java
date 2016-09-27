package com.coreoz.plume.db.hibernate.guice;

import com.coreoz.plume.db.hibernate.querydsl.HibernateToQuerydslBridge;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.google.inject.AbstractModule;

public class GuiceHibernateToQuerydslBridgeModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TransactionManagerQuerydsl.class).toProvider(HibernateToQuerydslBridge.class);
	}

}
