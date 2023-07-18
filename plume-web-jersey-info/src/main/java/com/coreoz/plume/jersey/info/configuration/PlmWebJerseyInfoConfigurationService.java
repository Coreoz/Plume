package com.coreoz.plume.jersey.info.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class PlmWebJerseyInfoConfigurationService {

    private final Config config;

    @Inject
    public PlmWebJerseyInfoConfigurationService(Config config) {
        this.config = config.withFallback(ConfigFactory.parseResources("plume-web-jersey-info.conf"));
    }

    public Map<String, Object> getCustomInfo() {
        return this.config.getObject("plm-web-jersey-info.info").unwrapped();
    }
}
