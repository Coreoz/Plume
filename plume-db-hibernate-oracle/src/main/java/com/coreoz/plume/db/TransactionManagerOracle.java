package com.coreoz.plume.db;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.coreoz.plume.db.TransactionManager;
import com.coreoz.plume.db.datasource.HibernateOracleFactory;
import com.typesafe.config.Config;

@Singleton
public class TransactionManagerOracle extends TransactionManager {

	@Inject
	public TransactionManagerOracle(Config config) {
		super(HibernateOracleFactory.newOracleContainer(config, "db"));
	}

}
