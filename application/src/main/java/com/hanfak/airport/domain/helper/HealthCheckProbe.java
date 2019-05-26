package com.hanfak.airport.domain.helper;

public interface HealthCheckProbe {
    ProbeResult probe();
    String name();
}
