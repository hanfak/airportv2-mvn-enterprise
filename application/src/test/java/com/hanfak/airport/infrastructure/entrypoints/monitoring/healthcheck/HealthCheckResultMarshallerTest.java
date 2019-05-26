package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;


import com.hanfak.airport.domain.monitoring.HealthCheckResult;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HealthCheckResultMarshallerTest {

  @Test
  public void createResponse() {
    when(healthCheckResultJsonBuilder.build(healthCheckResult)).thenReturn(jsonObject);
    when(jsonObject.toString(anyInt())).thenReturn(EXPECTED_JSON_STRING);

    RenderedContent content = marshaller.marshal(healthCheckResult);

    RenderedContent expectedContent = new RenderedContent(EXPECTED_JSON_STRING, "application/json", 200);
    assertThat(content).isEqualTo(expectedContent);
  }

  private static final String EXPECTED_JSON_STRING =
          "{\"optionalValues\":" +
                  "{\"applicationName\":\"Airport\"}," +
                  "\"probes\":[" +
                  "{\"name\":\"probeOne\",\"description\":\"http://whatever.com\",\"status\":\"OK\"}," +
                  "{\"name\":\"probeTwo\",\"description\":\"http://whatever.com\",\"status\":\"OK\"}]," +
                  "\"overallStatus\":\"OK\"}";

  private final HealthCheckResultJsonBuilder healthCheckResultJsonBuilder = mock(HealthCheckResultJsonBuilder.class);
  private final HealthCheckResult healthCheckResult = mock(HealthCheckResult.class);
  private final JSONObject jsonObject = mock(JSONObject.class);

  private final HealthCheckResultMarshaller marshaller = new HealthCheckResultMarshaller(healthCheckResultJsonBuilder);
}