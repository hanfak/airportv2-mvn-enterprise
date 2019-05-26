package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;

import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HealthCheckPageServletTest {

  @Test
  public void shouldReturnSuccessfulStatusResultWhenStatusCheckCompletesSuccessfully() throws Exception {
    when(healthCheckWebService.getStatus())
            .thenReturn(new RenderedContent( SUCCESSFUL_RESPONSE_BODY, EXPECTED_CONTENT_TYPE, 200));

    statusServlet.doGet(null, response);

    verifyResponseInteractions(200, EXPECTED_CONTENT_TYPE, SUCCESSFUL_RESPONSE_BODY);
  }

  @Test
  public void shouldReturnInternalServerErrorAndLogErrorWhenTheStatusWebServiceBlowsUp() throws Exception {
    RuntimeException expectedException = new RuntimeException("boom");
    when(healthCheckWebService.getStatus()).thenThrow(expectedException);

    statusServlet.doGet(null, response);

    String message = "Unable to retrieve health check report due to 'boom'";
    verifyResponseInteractions(503, "text/plain", message);
    verify(logger).error(message, expectedException);
  }

  private void verifyResponseInteractions(int statusCode, String expectedContentType, String responseBody) throws IOException {
    verify(healthCheckWebService).getStatus();
    verify(response).setStatus(statusCode);
    verify(response).getWriter();
    verify(response).setContentType(expectedContentType);
    verify(printerWriter).print(responseBody);
  }

  @Before
  public void setup() throws Exception {
    when(response.getWriter()).thenReturn(printerWriter);
  }

  private static final String SUCCESSFUL_RESPONSE_BODY = "{\"key\":\"this is the status result\"}";
  private static final String EXPECTED_CONTENT_TYPE = "application/json";
  private final HealthCheckWebService healthCheckWebService = mock(HealthCheckWebService.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final PrintWriter printerWriter = mock(PrintWriter.class);
  private final Logger logger = mock(Logger.class);

  private final HealthCheckPageServlet statusServlet = new HealthCheckPageServlet(healthCheckWebService, logger);
}