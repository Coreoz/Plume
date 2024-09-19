package com.coreoz.plume.jersey.monitoring.utils.health.healthchecks;

import com.codahale.metrics.health.HealthCheck;
import com.coreoz.plume.db.transaction.TransactionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DatabaseHealthCheck extends HealthCheck {
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
