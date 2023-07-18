package com.coreoz.plume.jersey.info.health;

import com.coreoz.plume.jersey.info.health.beans.HealthStatus;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.json.HealthCheckModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.SortedMap;

@Singleton
public class HealthCheckService {
    public static final HealthCheckModule objectMapperModule = new HealthCheckModule();

    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Inject
    private HealthCheckService() {
    }

    public void registerHealthCheck(String name, HealthCheck healthCheck) {
        this.healthCheckRegistry.register(name, healthCheck);
    }

    public HealthStatus isHealthy() {
        SortedMap<String, HealthCheck.Result> healthChecksResult = healthCheckRegistry.runHealthChecks();
        boolean isAppHealthy = healthChecksResult.values()
            .stream()
            .reduce(true, (isHealthy, healthCheck) -> isHealthy && healthCheck.isHealthy(), Boolean::logicalAnd);

        return new HealthStatus(isAppHealthy, healthChecksResult);
    }
}
