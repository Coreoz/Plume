package com.coreoz.plume.db.querydsl.dao;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.coreoz.plume.db.querydsl.crud.CrudDaoQuerydsl;
import com.coreoz.plume.db.querydsl.generated.QUser;
import com.coreoz.plume.db.querydsl.generated.User;
import com.coreoz.plume.db.querydsl.transaction.TransactionManagerQuerydsl;

@Singleton
public class UserDao extends CrudDaoQuerydsl<User> {

	@Inject
	public UserDao(TransactionManagerQuerydsl transactionManagerQuerydsl) {
		super(transactionManagerQuerydsl, QUser.user);
	}

}
