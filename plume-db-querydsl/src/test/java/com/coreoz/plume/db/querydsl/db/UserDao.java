package com.coreoz.plume.db.querydsl.db;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.coreoz.plume.db.querydsl.crud.CrudDaoQuerydsl;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;

@Singleton
public class UserDao extends CrudDaoQuerydsl<User> {

	@Inject
	public UserDao(TransactionManagerQuerydsl transactionManagerQuerydsl) {
		super(transactionManagerQuerydsl, QUser.user);
	}

}
