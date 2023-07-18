package com.coreoz.plume.jersey.info.health.healthchecks;

import com.codahale.metrics.health.HealthCheck;
import com.coreoz.plume.db.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DatabaseHealthCheck extends HealthCheck {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthCheck.class);
    private final TransactionManager transactionManager;

    public DatabaseHealthCheck(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Result check() {
        return transactionManager.executeAndReturn(connection -> {
            try {
                if (!connection.isValid(2)) {
                    logger.error("Cannot connect to database");
                    return Result.unhealthy("Cannot connect to database");
                }
                return Result.healthy();
            } catch (SQLException e) {
                logger.error("Cannot connect to database", e);
                return Result.unhealthy("Cannot connect to database");
            }
        });
    }
}
