package com.coreoz.plume.jersey.info.monitoring.health.beans;

import com.codahale.metrics.health.HealthCheck;

import java.util.SortedMap;

public class HealthStatus{
    private final boolean isHealthy;
    private final SortedMap<String, HealthCheck.Result> healthChecksResults;

    public HealthStatus(boolean isHealthy, SortedMap<String, HealthCheck.Result> healthChecksResults) {
        this.isHealthy = isHealthy;
        this.healthChecksResults = healthChecksResults;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public SortedMap<String, HealthCheck.Result> getHealthChecksResults() {
        return healthChecksResults;
    }
}
