package com.coreoz.plume.db.datasource;

import javax.persistence.EntityManagerFactory;

import org.sql2o.Sql2o;
import org.sql2o.quirks.OracleQuirks;

import com.coreoz.plume.db.datasource.DataSourceContainer;
import com.coreoz.plume.db.datasource.HibernateFactory;
import com.coreoz.plume.db.sql2o.BooleanConverter;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;

public class HibernateOracleFactory {

	public static DataSourceContainer newOracleContainer(Config config, String databasePrefix) {
		EntityManagerFactory entityManagerFactory = HibernateFactory.initializeEntityManagerFactory(config, databasePrefix);

		return new DataSourceContainer(
			entityManagerFactory,
			newSql2oOracle(entityManagerFactory)
		);
	}

	public static Sql2o newSql2oOracle(EntityManagerFactory entityManagerFactory) {
		BooleanConverter booleanConverter = new BooleanConverter();
		return new Sql2o(
			HibernateFactory.applicationDataSource(entityManagerFactory),
			new OracleQuirks(ImmutableMap.of(
				Boolean.class, booleanConverter,
				boolean.class, booleanConverter
			))
		);
	}

}
