package com.coreoz.plume.jersey.info.json;

import com.codahale.metrics.json.HealthCheckModule;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.concurrent.TimeUnit;

public class JerseyInfoObjectMapper {
    private static final HealthCheckModule healthCheckModule = new HealthCheckModule();
    private static final MetricsModule metricsModule = new MetricsModule(
        TimeUnit.SECONDS,
        TimeUnit.SECONDS,
        false
    );

    private JerseyInfoObjectMapper() {
    }

    public static ObjectMapper get() {
        return new ObjectMapper()
            .registerModule(healthCheckModule)
            .registerModule(metricsModule)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
