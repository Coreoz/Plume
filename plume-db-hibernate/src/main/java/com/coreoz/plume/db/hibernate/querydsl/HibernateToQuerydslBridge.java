package com.coreoz.plume.db.hibernate.querydsl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.coreoz.plume.db.hibernate.TransactionManagerHibernate;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;
import com.typesafe.config.Config;

@Singleton
public class HibernateToQuerydslBridge implements Provider<TransactionManagerQuerydsl> {

	private final TransactionManagerQuerydsl transactionManagerQuerydsl;

	@Inject
	public HibernateToQuerydslBridge(Config config,
			TransactionManagerHibernate transactionManagerHibernate) {
		String hibernateDialect = config.getObject("db").get("hibernate.dialect").toString();

		this.transactionManagerQuerydsl = new TransactionManagerQuerydsl(
			transactionManagerHibernate.dataSource(),
			new Configuration(toSQLTemplates(hibernateDialect))
		);
	}


	private SQLTemplates toSQLTemplates(String hibernateDialect) {
		if(hibernateDialect == null) {
			throw new IllegalArgumentException(" config key 'db.\"hibernate.dialect\"' have to be definied to use "
					+ "the hibernate to querydsl bridge");
		}

		String hibernateDialectLowercase = hibernateDialect.toLowerCase();
		if(hibernateDialectLowercase.contains("mysql")) {
			return MySQLTemplates.DEFAULT;
		}
		if(hibernateDialectLowercase.contains("h2")) {
			return H2Templates.DEFAULT;
		}
		if(hibernateDialectLowercase.contains("oracle")) {
			return OracleTemplates.DEFAULT;
		}
		if(hibernateDialectLowercase.contains("postgres")) {
			return PostgreSQLTemplates.DEFAULT;
		}
		if(hibernateDialectLowercase.contains("sqlserver")) {
			return SQLServerTemplates.DEFAULT;
		}

		throw new IllegalArgumentException("Hibernate dialect " + hibernateDialect + " is not recognized");
	}

	@Override
	public TransactionManagerQuerydsl get() {
		return transactionManagerQuerydsl;
	}

}
