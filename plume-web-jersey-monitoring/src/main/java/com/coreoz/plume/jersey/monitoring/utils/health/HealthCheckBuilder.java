package com.coreoz.plume.jersey.monitoring.utils.health;

import com.coreoz.plume.db.transaction.TransactionManager;
import com.coreoz.plume.jersey.monitoring.utils.health.beans.HealthStatus;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.coreoz.plume.jersey.monitoring.utils.health.healthchecks.DatabaseHealthCheck;

import jakarta.annotation.Nonnull;
import jakarta.inject.Provider;
import java.util.SortedMap;

public class HealthCheckBuilder {
    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Nonnull
    public HealthCheckBuilder registerHealthCheck(@Nonnull String name, @Nonnull HealthCheck healthCheck) {
        this.healthCheckRegistry.register(name, healthCheck);
        return this;
    }

    @Nonnull
    public HealthCheckBuilder registerDatabaseHealthCheck(@Nonnull TransactionManager transactionManager) {
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
