package com.coreoz.plume.jersey.monitoring.utils.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.coreoz.plume.jersey.grizzly.GrizzlyThreadPoolProbe;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.inject.Provider;
import java.util.Map;

public class MetricsCheckBuilder {
    private final MetricRegistry metricRegistry = new MetricRegistry();

    public MetricsCheckBuilder registerMetric(String name, Metric metric) {
        if (metric instanceof MetricSet metricSet) {
            this.metricRegistry.registerAll(name, metricSet);
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

    public MetricsCheckBuilder registerGrizzlyMetrics(GrizzlyThreadPoolProbe grizzlyThreadPoolProbe) {
        this.metricRegistry.register("http-pool.max-size", (Gauge<Integer>) grizzlyThreadPoolProbe::getPoolMaxSize);
        this.metricRegistry.register("http-pool.current-size", (Gauge<Integer>) grizzlyThreadPoolProbe::getTasksWaitingSize);
        this.metricRegistry.register("http-pool.waiting-size", (Gauge<Integer>) grizzlyThreadPoolProbe::getTasksWaitingSize);
        this.metricRegistry.register("http-pool.usage-size", (Gauge<Integer>) grizzlyThreadPoolProbe::getPoolUsageSize);
        this.metricRegistry.register("http-pool.usage", (Gauge<Float>) () -> ((float) grizzlyThreadPoolProbe.getPoolUsageSize()) / ((float)grizzlyThreadPoolProbe.getPoolMaxSize()));
        return this;
    }

    public MetricsCheckBuilder registerHikariMetrics(HikariDataSource hikariDataSource) {
        hikariDataSource.setMetricRegistry(this.metricRegistry);
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
