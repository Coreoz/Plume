package com.coreoz.plume.db;

import com.coreoz.plume.db.datasource.HibernateOracleFactory;
import com.typesafe.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionManagerOracle extends TransactionManager {

	@Inject
	public TransactionManagerOracle(Config config) {
		super(HibernateOracleFactory.newOracleContainer(config, "db"));
	}

}
