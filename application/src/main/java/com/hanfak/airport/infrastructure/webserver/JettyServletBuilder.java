package com.hanfak.airport.infrastructure.webserver;

import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics.PrometheusMetricsServlet;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.ready.ReadyServlet;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffServlet;
import com.hanfak.airport.infrastructure.webserver.notfound.NotFoundServlet;
import io.prometheus.client.CollectorRegistry;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;

import javax.servlet.http.HttpServlet;
import java.util.EnumSet;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.AUDIT;
import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zalando.logbook.DefaultHttpLogWriter.Level.INFO;

@SuppressWarnings("PMD.TooManyStaticImports") // This does not affect readability of code
public class JettyServletBuilder {

  private final ServletContextHandler servletContextHandler;
  private final JettyWebServer webServer;
  private final Logger logger;

  public JettyServletBuilder(ServletContextHandler servletContextHandler, JettyWebServer webServer, Logger logger) {
    this.servletContextHandler = servletContextHandler;
    this.webServer = webServer;
    this.logger = logger;
  }

  public JettyWebServer build(StatisticsHandler statisticsHandler) {
    addLoggingFilter();
    addServlet(new NotFoundServlet(), "/");
    addErrorHandler(new UncaughtErrorHandler(logger));
    statisticsHandler.setHandler(servletContextHandler);
    return webServer.withHandler(statisticsHandler);
  }

  public JettyServletBuilder registerLandAirplaneEndPoint(EndPoint endPoint, LandAirplaneServlet servlet) {
    addServlet(servlet, endPoint);
    return this;
  }

  public JettyServletBuilder registerAirplaneTakeOffEndPoint(EndPoint endPoint, AirplaneTakeOffServlet servlet) {
    addServlet(servlet, endPoint);
    return this;
  }

  public JettyServletBuilder registerReadyPageEndPoint(EndPoint endPoint, ReadyServlet servlet) {
    addServlet(servlet, endPoint);
    return this;
  }

  public JettyServletBuilder registerMetricsEndPoint(EndPoint endPoint, CollectorRegistry registry) {
    addServlet(new PrometheusMetricsServlet(registry, logger), endPoint);
    return this;
  }

  private void addServlet(HttpServlet httpServlet, EndPoint endPoint) {
    addServlet(httpServlet, endPoint.path);
  }

  private void addServlet(HttpServlet httpServlet, String path) {
    servletContextHandler.addServlet(new ServletHolder(httpServlet), path);
  }

  private void addErrorHandler(UncaughtErrorHandler uncaughtErrorHandler) {
    webServer.withBean(uncaughtErrorHandler);
  }

  private void addLoggingFilter() {
    Logbook logbook = Logbook.builder()
            .writer(new DefaultHttpLogWriter(getLogger(AUDIT.name()), INFO))
            .build();
    FilterHolder filterHolder = new FilterHolder(new LogbookFilter(logbook));
    servletContextHandler.addFilter(filterHolder, "/*", EnumSet.of(REQUEST, ASYNC, ERROR));
  }
}
