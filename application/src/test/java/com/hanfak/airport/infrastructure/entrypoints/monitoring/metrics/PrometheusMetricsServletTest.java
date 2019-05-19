package com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Vector;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PrometheusMetricsServletTest implements WithAssertions {

    @Test
    public void shouldReturnSuccessfulMetricsResultWhenMetricsReportCompletesSuccessfully() throws Exception {
        when(registry.filteredMetricFamilySamples(any())).thenReturn(new Vector<Collector.MetricFamilySamples>().elements());
        when(request.getParameterValues(any())).thenReturn(new String[0]);

        prometheusMetricsServlet.doGet(request, response);

        verifyResponseInteractions(200, EXPECTED_CONTENT_TYPE);
    }

    @Test
    public void shouldLogExceptionAndReturnErrorPage() throws Exception {
        IllegalStateException expectedException = new IllegalStateException("Boom!");
        when(registry.filteredMetricFamilySamples(any())).thenThrow(expectedException);
        when(request.getParameterValues(any())).thenReturn(new String[0]);

        prometheusMetricsServlet.doGet(request, response);

        verify(logger).error("Unable to retrieve metrics due to 'Boom!'", expectedException);
        verify(response).setContentType("text/plain");
        verify(response).setStatus(503);
        verify(printerWriter).print("Unable to retrieve metrics due to 'Boom!'");
    }

    @SuppressWarnings("SameParameterValue")
    private void verifyResponseInteractions(int statusCode, String expectedContentType) {
        verify(response).setStatus(statusCode);
        verify(response).setContentType(expectedContentType);
        verify(printerWriter).flush();
        verify(printerWriter).close();
    }

    @Before
    public void setup() throws Exception {
        when(response.getWriter()).thenReturn(printerWriter);
    }

    private static final String EXPECTED_CONTENT_TYPE = "text/plain; version=0.0.4; charset=utf-8";

    private final CollectorRegistry registry = mock(CollectorRegistry.class);
    private final Logger logger = mock(Logger.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final PrintWriter printerWriter = mock(PrintWriter.class);

    private final PrometheusMetricsServlet prometheusMetricsServlet = new PrometheusMetricsServlet(registry, logger);
}