package com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.hotspot.*;
import org.eclipse.jetty.server.handler.StatisticsHandler;

public final class RegisterMetrics {

    public static CollectorRegistry registerMetrics(StatisticsHandler statisticsHandler, CollectorRegistry registry) {
        registerMetrics(registry);
        registry.register(new JettyStatisticsCollector(statisticsHandler));
        return registry;
    }

    public static CollectorRegistry registerMetrics(CollectorRegistry registry) {
        registry.register(new StandardExports());
        registry.register(new MemoryPoolsExports());
        registry.register(new GarbageCollectorExports());
        registry.register(new ThreadExports());
        registry.register(new ClassLoadingExports());
        registry.register(new VersionInfoExports());
        return registry;
    }
}
