package com.coreoz.plume.jersey.monitoring.utils.health.beans;

import com.codahale.metrics.health.HealthCheck;

import java.util.Map;
import java.util.SortedMap;

public class HealthStatus {
    private final boolean isHealthy;
    private final Map<String, HealthCheck.Result> healthChecksResults;

    public HealthStatus(boolean isHealthy, SortedMap<String, HealthCheck.Result> healthChecksResults) {
        this.isHealthy = isHealthy;
        this.healthChecksResults = Map.copyOf(healthChecksResults);
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public Map<String, HealthCheck.Result> getHealthChecksResults() {
        return healthChecksResults;
    }
}
