package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;

import com.hanfak.airport.domain.monitoring.HealthCheckResult;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckResultJsonBuilderTest {

  @Test
  public void shouldBuildJsonObjectFromStatusResult() {
    ProbeResult probeOne = ProbeResult.success("probeOne", "http://whatever.com");
    ProbeResult probeTwo = ProbeResult.success("probeTwo", "http://whatever.com");
    List<ProbeResult> probeResults = Arrays.asList(probeOne, probeTwo);
    HealthCheckResult statusResult = new HealthCheckResult(probeResults);

    JSONObject actual = jsonStatusResponseBuilder.build(statusResult);

    assertThat(actual.toString()).isEqualTo(EXPECTED_JSON_STRING);
  }

  private static final String EXPECTED_JSON_STRING =
          "{\"optionalValues\":" +
                  "{\"applicationName\":\"Airport\"}," +
                  "\"probes\":[" +
                  "{\"name\":\"probeOne\",\"description\":\"http://whatever.com\",\"status\":\"OK\"}," +
                  "{\"name\":\"probeTwo\",\"description\":\"http://whatever.com\",\"status\":\"OK\"}]," +
                  "\"overallStatus\":\"OK\"}";

  private final HealthCheckResultJsonBuilder jsonStatusResponseBuilder = new HealthCheckResultJsonBuilder();
}