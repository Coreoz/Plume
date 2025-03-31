package com.coreoz.plume.jersey.monitoring.guice;

import com.coreoz.plume.jersey.guice.GuiceJacksonModule;
import com.coreoz.plume.jersey.monitoring.json.JerseyMonitoringObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;

/**
 * @deprecated {@link GuiceJacksonModule} should be used instead, there are no more reasons to use this module.
 *
 * TODO to be removed in Plume 6.0.0
 */
@Deprecated
public class GuiceJacksonWithMetricsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(JerseyMonitoringObjectMapperProvider.class);
    }
}
