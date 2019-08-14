package com.hanfak.airport.domain.monitoring;

import java.util.concurrent.Callable;

public interface HealthCheckProbe extends Callable<ProbeResult> {
    ProbeResult probe();
    String name();
    @Override
    default ProbeResult call() {
        return probe();
    }
}
