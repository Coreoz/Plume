package com.coreoz.plume.db.hibernate.datasource;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;

import com.google.common.base.Throwables;
import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariDataSource;

public class HibernateFactory {

	public static DataSourceContainer newContainer(Config config, String databasePrefix) {
		EntityManagerFactory entityManagerFactory = initializeEntityManagerFactory(config, databasePrefix);

		return new DataSourceContainer(
			entityManagerFactory,
			newSql2oNoQuirks(entityManagerFactory)
		);
	}

	public static Sql2o newSql2oNoQuirks(EntityManagerFactory entityManagerFactory) {
		return new Sql2o(applicationDataSource(entityManagerFactory), new NoQuirks());
	}

	/**
	 * Initialise une EntityManagerFactory depuis une configuration et un préfixe de base de données dans la configuration
	 * @param config Une configuration déjà initialisée
	 * @param databasePrefix Un préfixe, par exemple "db" pour lire les valeurs comme <code>db."hibernate.connection.provider_class"</code>
	 */
	public static EntityManagerFactory initializeEntityManagerFactory(Config config, String databasePrefix) {
		Map<String, String> dbProperties = config
				.getObject(databasePrefix)
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, value -> value.getValue().unwrapped().toString()));

		return Persistence.createEntityManagerFactory("jpa-unit", dbProperties);
	}

	/**
	 * Extrait une DataSource d'une EntityManagerFactory
	 */
	public static DataSource applicationDataSource(EntityManagerFactory entityManagerFactory) {
		EntityManagerImpl entityManagerImpl = (EntityManagerImpl) entityManagerFactory.createEntityManager();
		try {
			HikariCPConnectionProvider hikariCPConnectionProvider = (HikariCPConnectionProvider) entityManagerImpl.getFactory().getSessionFactory().getServiceRegistry().getService(ConnectionProvider.class);

			Field dataSourceField = HikariCPConnectionProvider.class.getDeclaredField("hds");
			dataSourceField.setAccessible(true);
			return (HikariDataSource) dataSourceField.get(hikariCPConnectionProvider);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		} finally {
			entityManagerImpl.close();
		}
	}

}
