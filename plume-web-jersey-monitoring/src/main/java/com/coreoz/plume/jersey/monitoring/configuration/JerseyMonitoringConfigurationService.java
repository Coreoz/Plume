package com.coreoz.plume.jersey.monitoring.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;

@Singleton
public class JerseyMonitoringConfigurationService {
    private final Config config;

    @Inject
    public JerseyMonitoringConfigurationService(Config config) {
        this.config = config.withFallback(ConfigFactory.parseResources("plume-web-jersey-monitoring.conf"));
    }

    public Map<String, Object> getCustomInfo() {
        return this.config.getObject("plm-web-jersey-info.info").unwrapped();
    }
}
