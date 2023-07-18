package com.coreoz.plume.jersey.info.metrics;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.json.MetricsModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public class MetricsService {
    public static final MetricsModule objectMapperModule = new MetricsModule(TimeUnit.SECONDS, TimeUnit.SECONDS, false);

    private final MetricRegistry metricRegistry = new MetricRegistry();

    @Inject
    private MetricsService() {
    }

    public void registerMetric(String name, Metric metric) {
        this.metricRegistry.register(name, metric);
    }

    public Map<String, Metric> getMetrics() {
        return this.metricRegistry.getMetrics();
    }
}
