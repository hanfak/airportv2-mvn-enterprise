package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.crosscutting.Concurrently;
import com.hanfak.airport.domain.crosscutting.SupplierCaching;
import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.HealthCheckResult;
import com.hanfak.airport.domain.monitoring.ProbeResult;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

// TODO scheduled job to run this usecase/servlet regularly
public class HealthChecksUseCase {

  private final List<HealthCheckProbe> healthCheckProbes;
  private final Supplier<HealthCheckResult> healthCheckResult;
  private final Concurrently concurrently;

  public HealthChecksUseCase(List<HealthCheckProbe> healthCheckProbes, SupplierCaching supplierCaching, Concurrently concurrently) {
    this.healthCheckProbes = healthCheckProbes;
    this.healthCheckResult = supplierCaching.cacheForDuration(this::doGetStatus, Duration.ofSeconds(0)); // TODO should be a property
    this.concurrently = concurrently;
  }

  public HealthCheckResult getStatus() {
    return healthCheckResult.get();
  }

  private HealthCheckResult doGetStatus() {
    List<ProbeResult> probeResults = concurrently.execute(healthCheckProbes);
    return new HealthCheckResult(probeResults);
  }
}
