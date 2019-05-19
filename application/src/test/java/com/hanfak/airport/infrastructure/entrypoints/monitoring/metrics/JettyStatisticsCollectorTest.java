package com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics;

import io.prometheus.client.CollectorRegistry;
import org.assertj.core.api.WithAssertions;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics.RegisterMetrics.registerMetrics;

public class JettyStatisticsCollectorTest implements WithAssertions {

  @Test
  public void collect() throws Exception {
    int port = startServerWithJettyStatsCollector(server);

    // send GET request
    try {
      HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://127.0.0.1:" + port + "/metrics").openConnection();
      urlConnection.getInputStream().close();
      urlConnection.disconnect();
    } catch (FileNotFoundException ignored) {
    }

    assertThat(collectorRegistry.getSampleValue("http_requests_total")).describedAs("http_requests_total").isEqualTo(1.0);
    assertThat(collectorRegistry.getSampleValue("http_requests_active")).describedAs("http_requests_active").isLessThanOrEqualTo(1.0);
    assertThat(collectorRegistry.getSampleValue("http_requests_active_max")).describedAs("http_requests_active_max").isEqualTo(1.0);
    assertThat(collectorRegistry.getSampleValue("http_request_time_max_seconds")).describedAs("http_request_time_max_seconds").isGreaterThan(0).isLessThan(10);
    assertThat(collectorRegistry.getSampleValue("http_request_time_seconds_total")).describedAs("http_request_time_seconds_total").isGreaterThan(0).isLessThan(10);
    assertThat(collectorRegistry.getSampleValue("http_dispatched_total")).describedAs("http_dispatched_total").isEqualTo(1.0);
    assertThat(collectorRegistry.getSampleValue("http_dispatched_active")).describedAs("http_dispatched_active").isLessThanOrEqualTo(1.0);
    assertThat(collectorRegistry.getSampleValue("http_dispatched_active_max")).describedAs("http_dispatched_active_max").isGreaterThan(0.0);
    assertThat(collectorRegistry.getSampleValue("http_dispatched_time_max")).describedAs("http_dispatched_time_max").isGreaterThan(0);
    assertThat(collectorRegistry.getSampleValue("http_dispatched_time_seconds_total")).describedAs("http_dispatched_time_seconds_total").isGreaterThan(0).isLessThan(10);
    assertThat(collectorRegistry.getSampleValue("http_async_requests_total")).describedAs("http_async_requests_total").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_async_requests_waiting")).describedAs("http_async_requests_waiting").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_async_requests_waiting_max")).describedAs("http_async_requests_waiting_max").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_async_dispatches_total")).describedAs("http_async_dispatches_total").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_expires_total")).describedAs("http_expires_total").isEqualTo(0.0);

    assertThat(collectorRegistry.getSampleValue("http_responses",
            new String[]{"code"}, new String[]{"1xx"})).describedAs("1xx").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_responses",
            new String[]{"code"}, new String[]{"2xx"})).describedAs("2xx").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_responses",
            new String[]{"code"}, new String[]{"3xx"})).describedAs("3xx").isEqualTo(0.0);
    assertThat(collectorRegistry.getSampleValue("http_responses",
            new String[]{"code"}, new String[]{"4xx"})).describedAs("4xx").isEqualTo(1.0);
    assertThat(collectorRegistry.getSampleValue("http_responses",
            new String[]{"code"}, new String[]{"5xx"})).describedAs("5xx").isEqualTo(0.0);

    assertThat(collectorRegistry.getSampleValue("http_stats_seconds")).describedAs("http_stats_seconds").isGreaterThan(0).isLessThan(10);
    assertThat(collectorRegistry.getSampleValue("http_responses_bytes_total")).describedAs("http_responses_bytes_total").isGreaterThan(0);
  }

  private int startServerWithJettyStatsCollector(Server server) throws Exception {
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/metrics");
    server.setHandler(context);

    HandlerCollection handlers = new HandlerCollection();

    handlers.addHandler(new DefaultHandler());

    StatisticsHandler statisticsHandler = new StatisticsHandler();
    statisticsHandler.setHandler(handlers);

    // register collector
    collectorRegistry = registerMetrics(statisticsHandler, new CollectorRegistry(true));


    server.setHandler(statisticsHandler);

    server.start();

    ServerConnector connector = (ServerConnector) server.getConnectors()[0];
    return connector.getLocalPort();
  }

  @After
  public void stopServer() throws Exception {
    server.stop();
  }

  private final Server server = new Server(0);
  private CollectorRegistry collectorRegistry;
}
