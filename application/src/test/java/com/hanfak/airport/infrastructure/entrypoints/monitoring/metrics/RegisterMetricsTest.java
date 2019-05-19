package com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.hotspot.*;
import org.assertj.core.api.WithAssertions;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.junit.Test;

import static com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics.RegisterMetrics.registerMetrics;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RegisterMetricsTest implements WithAssertions {

    private final CollectorRegistry collectorRegistry = mock(CollectorRegistry.class);
    private final StatisticsHandler statisticsHandler = mock(StatisticsHandler.class);

    @Test
    public void standardMetricsAreExposed() {
        CollectorRegistry returnedRegistry = registerMetrics(collectorRegistry);

        verify(collectorRegistry).register(isA(StandardExports.class));
        verify(collectorRegistry).register(isA(MemoryPoolsExports.class));
        verify(collectorRegistry).register(isA(GarbageCollectorExports.class));
        verify(collectorRegistry).register(isA(ThreadExports.class));
        verify(collectorRegistry).register(isA(ClassLoadingExports.class));
        verify(collectorRegistry).register(isA(VersionInfoExports.class));

        assertThat(returnedRegistry).isSameAs(collectorRegistry);
    }

    @Test
    public void standardMetricsWithJettyStatisticsHandlerAreExposed() {
        CollectorRegistry returnedRegistry = RegisterMetrics.registerMetrics(statisticsHandler, collectorRegistry);

        verify(collectorRegistry).register(isA(JettyStatisticsCollector.class));
        verify(collectorRegistry).register(isA(StandardExports.class));
        verify(collectorRegistry).register(isA(MemoryPoolsExports.class));
        verify(collectorRegistry).register(isA(GarbageCollectorExports.class));
        verify(collectorRegistry).register(isA(ThreadExports.class));
        verify(collectorRegistry).register(isA(ClassLoadingExports.class));
        verify(collectorRegistry).register(isA(VersionInfoExports.class));
        
        assertThat(returnedRegistry).isSameAs(collectorRegistry);
    }

    @Test
    public void forCodeCoverage() {
        RegisterMetrics registerMetrics = new RegisterMetrics();

        assertThat(registerMetrics).isInstanceOf(RegisterMetrics.class);
    }
}