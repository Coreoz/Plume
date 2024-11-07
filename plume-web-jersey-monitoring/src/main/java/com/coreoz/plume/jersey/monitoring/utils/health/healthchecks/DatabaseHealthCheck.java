package com.coreoz.plume.jersey.monitoring.utils.health.healthchecks;

import com.codahale.metrics.health.HealthCheck;
import com.coreoz.plume.db.transaction.TransactionManager;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DatabaseHealthCheck extends HealthCheck {
    private static final String FAIL_TO_CONNECT_TO_DATABASE_ERROR = "Cannot connect to database";

    private final TransactionManager transactionManager;

    public DatabaseHealthCheck(@Nonnull TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Result check() {
        return transactionManager.executeAndReturn(connection -> {
            try {
                if (!connection.isValid(2)) {
                    logger.error(FAIL_TO_CONNECT_TO_DATABASE_ERROR);
                    return Result.unhealthy(FAIL_TO_CONNECT_TO_DATABASE_ERROR);
                }
                return Result.healthy();
            } catch (SQLException e) {
                logger.error(FAIL_TO_CONNECT_TO_DATABASE_ERROR, e);
                return Result.unhealthy(FAIL_TO_CONNECT_TO_DATABASE_ERROR);
            }
        });
    }
}
