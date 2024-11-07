package com.coreoz.plume.db.querydsl.transaction;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;
import jakarta.annotation.Nonnull;

enum QuerydslTemplates {

	MYSQL(MySQLTemplates.DEFAULT),
	H2(new H2Templates(true)),
	ORACLE(OracleTemplates.DEFAULT),
	POSTGRE(PostgreSQLTemplates.DEFAULT),
	SQL_SERVEUR(SQLServerTemplates.DEFAULT),
	;

	private final SQLTemplates sqlTemplates;

	private QuerydslTemplates(@Nonnull SQLTemplates sqlTemplates) {
		this.sqlTemplates = sqlTemplates;
	}

	public SQLTemplates sqlTemplates() {
		return sqlTemplates;
	}

}
