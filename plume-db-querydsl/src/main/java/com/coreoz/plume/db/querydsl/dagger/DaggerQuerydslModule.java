package com.coreoz.plume.db.querydsl.dagger;

import jakarta.inject.Singleton;

import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.coreoz.plume.db.transaction.TransactionManager;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerQuerydslModule {

	@Provides
	@Singleton
	static TransactionManager provideTransactionManager(TransactionManagerQuerydsl transactionManagerQuerydsl) {
		return transactionManagerQuerydsl;
	}

}
