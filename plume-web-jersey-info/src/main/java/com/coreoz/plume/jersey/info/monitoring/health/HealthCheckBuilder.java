package com.coreoz.plume.jersey.info.monitoring.health;

import com.coreoz.plume.db.transaction.TransactionManager;
import com.coreoz.plume.jersey.info.monitoring.health.beans.HealthStatus;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.coreoz.plume.jersey.info.monitoring.health.healthchecks.DatabaseHealthCheck;

import javax.inject.Provider;
import java.util.SortedMap;

public class HealthCheckBuilder {
    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    private HealthCheckBuilder() {
    }

    public HealthCheckBuilder registerHealthCheck(String name, HealthCheck healthCheck) {
        this.healthCheckRegistry.register(name, healthCheck);
        return this;
    }

    public HealthCheckBuilder registerDatabaseHealthCheck(TransactionManager transactionManager) {
        this.healthCheckRegistry.register("database", new DatabaseHealthCheck(transactionManager));
        return this;
    }

    public Provider<HealthStatus> build() {
        return this::isHealthy;
    }

    /* PRIVATE */
    private HealthStatus isHealthy() {
        SortedMap<String, HealthCheck.Result> healthChecksResult = healthCheckRegistry.runHealthChecks();
        boolean isAppHealthy = healthChecksResult.values()
            .stream()
            .reduce(true, (isHealthy, healthCheck) -> isHealthy && healthCheck.isHealthy(), Boolean::logicalAnd);

        return new HealthStatus(isAppHealthy, healthChecksResult);
    }
}
