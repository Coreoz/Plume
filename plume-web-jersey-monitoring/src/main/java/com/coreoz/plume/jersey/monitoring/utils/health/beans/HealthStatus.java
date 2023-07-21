package com.coreoz.plume.jersey.monitoring.utils.health.beans;

import com.codahale.metrics.health.HealthCheck;
import lombok.Value;

import java.util.Map;

@Value
public class HealthStatus {
    boolean isHealthy;
    Map<String, HealthCheck.Result> healthChecksResults;
}
