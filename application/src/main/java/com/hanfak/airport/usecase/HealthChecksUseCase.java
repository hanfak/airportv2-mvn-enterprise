package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.HealthCheckResult;
import com.hanfak.airport.domain.monitoring.ProbeResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO when more probes added, call them all at once using Executor service dependency.
// TODO scheduled job to run this usecase/servlet regularly
public class HealthChecksUseCase {

  private final List<HealthCheckProbe> healthCheckProbes;

  public HealthChecksUseCase(List<HealthCheckProbe> healthCheckProbes) {
    this.healthCheckProbes = healthCheckProbes;
  }

  public HealthCheckResult getStatus() {
    List<ProbeResult> probeResults = healthCheckProbes.stream()
            .map(HealthCheckProbe::probe)
            .collect(toList());
    return new HealthCheckResult(probeResults);
  }
}
