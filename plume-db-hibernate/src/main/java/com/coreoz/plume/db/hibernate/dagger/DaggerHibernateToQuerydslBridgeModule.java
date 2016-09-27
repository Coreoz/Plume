package com.coreoz.plume.db.hibernate.dagger;

import javax.inject.Singleton;

import com.coreoz.plume.db.hibernate.querydsl.HibernateToQuerydslBridge;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerHibernateToQuerydslBridgeModule {

	@Provides
	@Singleton
	static TransactionManagerQuerydsl provideTransactionManagerQuerydsl(HibernateToQuerydslBridge hibernateToQuerydslBridge) {
		return hibernateToQuerydslBridge.get();
	}

}
