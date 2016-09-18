package com.coreoz.plume.db.hibernate;

import com.coreoz.plume.db.hibernate.TransactionManagerHibernate;
import com.coreoz.plume.db.hibernate.datasource.HibernateOracleFactory;
import com.typesafe.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionManagerOracle extends TransactionManagerHibernate {

	@Inject
	public TransactionManagerOracle(Config config) {
		super(HibernateOracleFactory.newOracleContainer(config, "db"));
	}

}
