package com.coreoz.plume.jersey.info.objectmapper;

import com.coreoz.plume.jersey.info.health.HealthCheckService;
import com.coreoz.plume.jersey.info.metrics.MetricsService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class PlmWebJerseyInfoObjectMapper extends ObjectMapper {
    public PlmWebJerseyInfoObjectMapper(){
        super();

        this.registerModule(HealthCheckService.objectMapperModule)
            .registerModule(MetricsService.objectMapperModule)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
