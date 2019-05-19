package com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

@SuppressWarnings("PMD.AvoidCatchingGenericException") // all exceptions will be logged and we need to present a generic error page to the users
public class PrometheusMetricsServlet extends MetricsServlet {

    private final Logger logger;

    public PrometheusMetricsServlet(CollectorRegistry registry, Logger logger) {
        super(registry);
        this.logger = logger;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            super.doGet(request, response);
        } catch (Exception e) {
            String errorMessage = format("Unable to retrieve metrics due to '%s'", e.getMessage());
            response.setStatus(503);
            response.setContentType("text/plain");
            response.getWriter().print(errorMessage);
            logger.error(errorMessage, e);
        }
    }
}
