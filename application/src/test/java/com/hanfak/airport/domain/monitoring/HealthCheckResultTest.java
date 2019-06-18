package com.hanfak.airport.domain.monitoring;

import org.junit.Test;

import static com.hanfak.airport.domain.monitoring.ProbeResult.failure;
import static com.hanfak.airport.domain.monitoring.ProbeResult.success;
import static com.hanfak.airport.domain.monitoring.ProbeResult.warn;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckResultTest {

  @Test
  public void shouldReturnOverallStatusOkWhenAllEndpointProbesWereSuccessful() throws Exception {
    HealthCheckResult healthCheckResult = new HealthCheckResult(asList(SUCCESSFUL_PROBE_RESULT_ONE, SUCCESSFUL_PROBE_RESULT_TWO));

    assertThat(healthCheckResult.getOverallStatus()).isEqualTo(ProbeStatus.OK);
  }

  @Test
  public void shouldReturnOverallStatusFailedWhenAnyOfTheEndpointProbesWereUnsuccessful() throws Exception {
    HealthCheckResult healthCheckResult = new HealthCheckResult(asList(SUCCESSFUL_PROBE_RESULT_ONE, FAILED_PROBE_RESULT, SUCCESSFUL_PROBE_RESULT_TWO, FAILED_PROBE_RESULT));

    assertThat(healthCheckResult.getOverallStatus()).isEqualTo(ProbeStatus.FAIL);
  }

  @Test
  public void shouldReturnOverallStatusWarnWhenAnyOfTheEndpointProbesWereWarningButNoneIsFail() throws Exception {
    HealthCheckResult healthCheckResult = new HealthCheckResult(asList(SUCCESSFUL_PROBE_RESULT_ONE, SUCCESSFUL_PROBE_RESULT_TWO, WARN_PROBE_RESULT));

    assertThat(healthCheckResult.getOverallStatus()).isEqualTo(ProbeStatus.WARN);
  }

  @Test
  public void shouldReturnOverallStatusErrorWhenTheEndpointProbeReturnOkWarningAndError() throws Exception {
    HealthCheckResult healthCheckResult = new HealthCheckResult(asList(SUCCESSFUL_PROBE_RESULT_ONE, SUCCESSFUL_PROBE_RESULT_TWO, WARN_PROBE_RESULT, FAILED_PROBE_RESULT));

    assertThat(healthCheckResult.getOverallStatus()).isEqualTo(ProbeStatus.FAIL);
  }

  private static final ProbeResult SUCCESSFUL_PROBE_RESULT_ONE = success("name1", "endpoint1");
  private static final ProbeResult SUCCESSFUL_PROBE_RESULT_TWO =  success("name2", "endpoint2");
  private static final ProbeResult WARN_PROBE_RESULT =  warn("name3", "endpoint3");
  private static final ProbeResult FAILED_PROBE_RESULT = failure("name2", "description");
}