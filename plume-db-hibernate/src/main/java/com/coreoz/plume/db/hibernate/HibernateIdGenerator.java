package com.coreoz.plume.db.hibernate;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.coreoz.plume.db.utils.IdGenerator;

public class HibernateIdGenerator implements IdentifierGenerator {

	public static final String NAME = "plume";

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		return IdGenerator.generate();
	}


}
