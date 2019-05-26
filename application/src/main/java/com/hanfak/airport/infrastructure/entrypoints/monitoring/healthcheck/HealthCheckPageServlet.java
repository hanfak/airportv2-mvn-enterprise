package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;

import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class HealthCheckPageServlet extends HttpServlet {

  private final HealthCheckWebService healthCheckWebService;
  private final Logger logger;

  public HealthCheckPageServlet(HealthCheckWebService healthCheckWebService, Logger logger) {
    this.healthCheckWebService = healthCheckWebService;
    this.logger = logger;
  }

  @Override
  @SuppressWarnings({"PMD.AvoidCatchingGenericException", "RedundantThrows"})
  //catch all exceptions to wrap a nicer error message to external consumers
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      RenderedContent renderedContent = healthCheckWebService.getStatus();
      renderedContent.render(response);
    } catch (Exception e) {
      String errorMessage = format("Unable to retrieve health check report due to '%s'", e.getMessage());
      new RenderedContent(errorMessage, "text/plain", 503).render(response);
      logger.error(errorMessage, e);
    }
  }
}
