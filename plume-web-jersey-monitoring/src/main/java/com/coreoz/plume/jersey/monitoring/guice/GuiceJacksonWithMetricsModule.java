package com.coreoz.plume.jersey.monitoring.guice;

import com.coreoz.plume.jersey.monitoring.json.JerseyMonitoringObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

public class GuiceJacksonWithMetricsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(JerseyMonitoringObjectMapperProvider.class);
    }
}
