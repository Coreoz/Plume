package com.coreoz.plume.jersey.monitoring.utils.metrics;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import jakarta.inject.Provider;
import java.util.Map;

public class MetricsCheckBuilder {
    private final MetricRegistry metricRegistry = new MetricRegistry();

    public MetricsCheckBuilder registerMetric(String name, Metric metric) {
        if (metric instanceof MetricSet) {
            this.metricRegistry.registerAll(name, (MetricSet) metric);
        } else {
            this.metricRegistry.register(name, metric);
        }
        return this;
    }

    public MetricsCheckBuilder registerJvmMetrics() {
        this.metricRegistry.registerAll("memory-usage", new MemoryUsageGaugeSet());
        this.metricRegistry.registerAll("thread-states", new ThreadStatesGaugeSet());
        return this;
    }

    public Provider<Map<String, Metric>> build() {
        return this::getMetrics;
    }

    /* PRIVATE */
    private Map<String, Metric> getMetrics() {
        return this.metricRegistry.getMetrics();
    }
}
