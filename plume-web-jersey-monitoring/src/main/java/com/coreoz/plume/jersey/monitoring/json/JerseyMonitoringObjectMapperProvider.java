package com.coreoz.plume.jersey.monitoring.json;

import com.codahale.metrics.json.HealthCheckModule;
import com.codahale.metrics.json.MetricsModule;
import com.coreoz.plume.jersey.jackson.ObjectMapperProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class JerseyMonitoringObjectMapperProvider implements Provider<ObjectMapper> {
    private final ObjectMapper objectMapper;

    @Inject
    public JerseyMonitoringObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
        this.objectMapper = objectMapperProvider.get()
            .registerModule(new HealthCheckModule())
            .registerModule(new MetricsModule(
                TimeUnit.SECONDS,
                TimeUnit.SECONDS,
                false
            ))
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public ObjectMapper get() {
        return this.objectMapper;
    }
}
