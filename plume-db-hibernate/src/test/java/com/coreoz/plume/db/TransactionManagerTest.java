package com.coreoz.plume.db;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sql2o.Connection;

import javax.inject.Inject;

@RunWith(GuiceTestRunner.class)
@GuiceModules(DbHibernateTestModule.class)
public class TransactionManagerTest {

	@Inject
	private TransactionManager transactionManager;

	@Test
	public void check_that_the_transaction_manager_is_correctly_initialized() {
		transactionManager.execute(em -> em.createQuery("from java.lang.Object").getResultList());
	}
	
	@Test
	public void check_that_sql2o_is_correctly_initialized() {
		try(Connection connection = transactionManager.sql2o().open()) {
			int one = connection
				.createQuery("SELECT 1")
				.executeScalar(Integer.class);
			TestCase.assertEquals(1, one);
		}
	}

}
