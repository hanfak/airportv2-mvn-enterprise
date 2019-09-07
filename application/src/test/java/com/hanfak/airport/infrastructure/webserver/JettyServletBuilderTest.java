package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import org.assertj.core.api.WithAssertions;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyServletBuilderTest implements WithAssertions {

  @Test
  public void shouldAddServletHandlerToServerWhenBuilt() {
    ArgumentCaptor<UncaughtErrorHandler> uncaughtErrorHandlerArgumentCaptor = ArgumentCaptor
            .forClass(UncaughtErrorHandler.class);
    ArgumentCaptor<CustomRequestLog> customRequestLogArgumentCaptor = ArgumentCaptor
            .forClass(CustomRequestLog.class);

    when(webServer.withHandler(any())).thenReturn(webServer);
    JettyServletBuilder.registerLandAirplaneEndPoint(EndPoint.get("/path"), servlet);
    JettyServletBuilder.build(handler);

    verify(handler).setHandler(servletHandler);
    verify(webServer).withHandler(handler);
    verify(webServer).withBean(uncaughtErrorHandlerArgumentCaptor.capture());
    verify(webServer).withRequestLog(customRequestLogArgumentCaptor.capture());
  }

  @Test
  public void shouldAddServletToHandler() {
    JettyServletBuilder.registerLandAirplaneEndPoint(EndPoint.post("/path"), servlet);

    verify(servletHandler).addServlet(any(ServletHolder.class), anyString());
  }

  private final ServletContextHandler servletHandler = mock(ServletContextHandler.class);
  private final JettyWebServer webServer = mock(JettyWebServer.class);
  private final LandAirplaneServlet servlet = mock(LandAirplaneServlet.class);
  private final StatisticsHandler handler = mock(StatisticsHandler.class);
  private final JettyServletBuilder JettyServletBuilder = new JettyServletBuilder(servletHandler, webServer, mock(Logger.class));
}

