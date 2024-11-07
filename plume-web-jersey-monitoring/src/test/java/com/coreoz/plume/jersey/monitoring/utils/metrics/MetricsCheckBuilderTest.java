package com.coreoz.plume.jersey.monitoring.utils.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.coreoz.plume.jersey.grizzly.GrizzlyThreadPoolProbe;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MetricsCheckBuilderTest {

    private MetricsCheckBuilder metricsCheckBuilder;

    @BeforeEach
    void setUp() {
        metricsCheckBuilder = new MetricsCheckBuilder();
    }

    @Test
    void registerMetric_should_registerSingleMetric() {
        Metric mockMetric = mock(Metric.class);

        metricsCheckBuilder.registerMetric("custom-metric", mockMetric);

        Map<String, Metric> registeredMetrics = metricsCheckBuilder.build().get();
        assertTrue(registeredMetrics.containsKey("custom-metric"));
        assertEquals(mockMetric, registeredMetrics.get("custom-metric"));
    }

    @Test
    void registerMetric_should_registerMetricSet() {
        MetricSet mockMetricSet = mock(MetricSet.class);
        Map<String, Metric> mockMetrics = Map.of("metric1", mock(Metric.class), "metric2", mock(Metric.class));
        when(mockMetricSet.getMetrics()).thenReturn(mockMetrics);

        metricsCheckBuilder.registerMetric("custom-metric-set", mockMetricSet);

        Map<String, Metric> registeredMetrics = metricsCheckBuilder.build().get();
        assertTrue(registeredMetrics.containsKey("custom-metric-set.metric1"));
        assertTrue(registeredMetrics.containsKey("custom-metric-set.metric2"));
    }

    @Test
    void registerJvmMetrics_should_registerJvmMetricsCorrectly() {
        metricsCheckBuilder.registerJvmMetrics();

        Map<String, Metric> registeredMetrics = metricsCheckBuilder.build().get();
        assertTrue(registeredMetrics.containsKey("memory-usage.heap.max"));
        assertTrue(registeredMetrics.containsKey("thread-states.runnable.count"));
    }

    @Test
    void registerGrizzlyMetrics_should_registerGrizzlyMetricsCorrectly() {
        GrizzlyThreadPoolProbe mockProbe = mock(GrizzlyThreadPoolProbe.class);
        when(mockProbe.getPoolMaxSize()).thenReturn(100);
        when(mockProbe.getTasksWaitingSize()).thenReturn(10);
        when(mockProbe.getPoolUsageSize()).thenReturn(50);

        metricsCheckBuilder.registerGrizzlyMetrics(mockProbe);

        Map<String, Metric> registeredMetrics = metricsCheckBuilder.build().get();
        assertTrue(registeredMetrics.containsKey("http-pool.max-size"));
        assertTrue(registeredMetrics.containsKey("http-pool.current-size"));
        assertTrue(registeredMetrics.containsKey("http-pool.waiting-size"));
        assertTrue(registeredMetrics.containsKey("http-pool.usage-size"));
        assertTrue(registeredMetrics.containsKey("http-pool.usage"));

        Gauge<Integer> maxSizeGauge = (Gauge<Integer>) registeredMetrics.get("http-pool.max-size");
        assertEquals(100, maxSizeGauge.getValue());

        Gauge<Integer> waitingSizeGauge = (Gauge<Integer>) registeredMetrics.get("http-pool.waiting-size");
        assertEquals(10, waitingSizeGauge.getValue());

        Gauge<Float> usageGauge = (Gauge<Float>) registeredMetrics.get("http-pool.usage");
        assertEquals(0.5f, usageGauge.getValue(), 0.01);
    }

    @Test
    void registerHikariMetrics_should_registerHikariMetricsCorrectly() {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);

        metricsCheckBuilder.registerHikariMetrics(mockDataSource);

        verify(mockDataSource).setMetricRegistry(any(MetricRegistry.class));
    }

    @Test
    void build_should_returnAllRegisteredMetrics() {
        Metric mockMetric1 = mock(Metric.class);
        Metric mockMetric2 = mock(Metric.class);

        metricsCheckBuilder
            .registerMetric("metric1", mockMetric1)
            .registerMetric("metric2", mockMetric2);

        Map<String, Metric> registeredMetrics = metricsCheckBuilder.build().get();
        assertEquals(2, registeredMetrics.size());
        assertTrue(registeredMetrics.containsKey("metric1"));
        assertTrue(registeredMetrics.containsKey("metric2"));
    }
}
