package com.coreoz.plume.jersey.monitoring.json;

import com.codahale.metrics.json.HealthCheckModule;
import com.codahale.metrics.json.MetricsModule;
import com.coreoz.plume.jersey.jackson.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
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
            ));
    }

    public ObjectMapper get() {
        return this.objectMapper;
    }
}
