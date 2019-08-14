package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.crosscutting.logging.LoggingUncaughtExceptionHandler;
import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.HealthCheckResult;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import com.hanfak.airport.domain.monitoring.ProbeStatus;
import com.hanfak.airport.infrastructure.crosscutting.ExecutorServiceConcurrently;
import com.hanfak.airport.infrastructure.crosscutting.GuavaSupplierCaching;
import com.hanfak.airport.infrastructure.crosscutting.TrackingExecutorServiceFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.hanfak.airport.domain.monitoring.ProbeResult.failure;
import static com.hanfak.airport.domain.monitoring.ProbeResult.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


// TODO move to integration test
public class HealthChecksUseCaseTest {

  @Test
  public void returnProbeResults() {
    when(probeOne.call()).thenReturn(success);
    when(probeTwo.call()).thenReturn(failure);

    HealthCheckResult result = healthChecksUseCase.getStatus();

    assertThat(result.getProbeResults()).containsExactly(success, failure);
    assertThat(result.getOverallStatus()).isEqualTo(ProbeStatus.FAIL);
  }

  private final HealthCheckProbe probeOne = mock(HealthCheckProbe.class);
  private final HealthCheckProbe probeTwo = mock(HealthCheckProbe.class);
  private final ProbeResult success = success("name", "description");
  private final ProbeResult failure = failure("name", "description");
  private final List<HealthCheckProbe> probes = Arrays.asList(probeOne, probeTwo);

  private final HealthChecksUseCase healthChecksUseCase = new HealthChecksUseCase(probes, new GuavaSupplierCaching(), new ExecutorServiceConcurrently(new TrackingExecutorServiceFactory(new LoggingUncaughtExceptionHandler(null)), probes.size()));
}