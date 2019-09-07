package com.hanfak.airport.infrastructure.webserver;

import org.eclipse.jetty.server.CustomRequestLog;
import org.junit.Test;

import static com.hanfak.airport.infrastructure.webserver.RequestLogFactory.createRequestLog;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestLogFactoryTest {
  @Test
  public void configureRequestLog() {
    CustomRequestLog requestLog = createRequestLog();
    assertThat(requestLog).isInstanceOf(CustomRequestLog.class);
  }
}