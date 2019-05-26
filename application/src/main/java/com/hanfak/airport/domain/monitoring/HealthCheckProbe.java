package com.hanfak.airport.domain.monitoring;

public interface HealthCheckProbe {
    ProbeResult probe();
    String name();
}
