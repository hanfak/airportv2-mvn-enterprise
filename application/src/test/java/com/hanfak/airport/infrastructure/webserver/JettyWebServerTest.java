package com.hanfak.airport.infrastructure.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JettyWebServerTest {

  private final Server server = mock(Server.class);
  private final Logger logger = mock(Logger.class);
  private final JettyWebServer webServer = new JettyWebServer(server, logger);
  private final URI uri = mock(URI.class);

  @Test
  public void shouldStartJettyServer() throws Exception {
    when(server.getURI()).thenReturn(URI.create("blah"));
    webServer.startServer();

    verify(server).start();
    verify(logger).info("Server started at: blah");
  }

  @Test
  public void shouldLogExceptionIfJettyThrowsExceptionOnStartup() throws Exception {
    when(server.getURI()).thenReturn(uri);
    when(uri.getPort()).thenReturn(1111);
    NullPointerException cause = new NullPointerException();
    doThrow(cause).when(server).start();

    assertThatThrownBy(webServer::startServer)
            .isInstanceOf(Exception.class);
    verify(logger).error("Could not start server on port '1111'", cause);
  }

  @Test
  public void shouldStopJettyServer() throws Exception {
    webServer.stopServer();

    verify(server).stop();
    verify(logger).info("Server stopped");
  }

  @Test
  public void shouldLogExceptionIfJettyThrowsExceptionOnShutdown() throws Exception {
    when(server.getURI()).thenReturn(uri);
    when(uri.getPort()).thenReturn(1111);
    NullPointerException cause = new NullPointerException();
    doThrow(cause).when(server).stop();

    assertThatThrownBy(webServer::stopServer)
            .isInstanceOf(Exception.class);
    verify(logger).error("Could not stop server on port '1111'", cause);
  }

  @Test
  public void shouldSetHandlerToJettyServer() {
    ServletContextHandler servletContextHandler = mock(ServletContextHandler.class);

    JettyWebServer jettyWebServer = webServer.withContext(servletContextHandler);

    assertThat(jettyWebServer).isInstanceOf(JettyWebServer.class);
    verify(server).setHandler(servletContextHandler);
  }

  @Test
  public void shouldSetBeanToJettyServer() {
    ErrorHandler errorHandler = mock(ErrorHandler.class);

    JettyWebServer jettyWebServer = webServer.withBean(errorHandler);

    assertThat(jettyWebServer).isInstanceOf(JettyWebServer.class);
    verify(server).addBean(errorHandler);
  }
}