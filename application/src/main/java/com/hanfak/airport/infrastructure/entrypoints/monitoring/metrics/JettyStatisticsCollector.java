package com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics;

import io.prometheus.client.Collector;
import org.eclipse.jetty.server.handler.StatisticsHandler;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class JettyStatisticsCollector extends Collector {
    private final StatisticsHandler statisticsHandler;

    public JettyStatisticsCollector(StatisticsHandler statisticsHandler) {
        this.statisticsHandler = statisticsHandler;
    }

    @Override
    public List<MetricFamilySamples> collect() {
        return asList(
                buildGauge("http_requests_total", "Number of requests", statisticsHandler.getRequests()),
                buildGauge("http_requests_active", "Number of requests currently active", statisticsHandler.getRequestsActive()),
                buildGauge("http_requests_active_max", "Maximum number of requests that have been active at once", statisticsHandler.getRequestsActiveMax()),
                buildGauge("http_request_time_max_seconds", "Maximum time spent handling requests", statisticsHandler.getRequestTimeMax() / 1000.0),
                buildGauge("http_request_time_seconds_total", "Total time spent in all request handling", statisticsHandler.getRequestTimeTotal() / 1000.0),
                buildGauge("http_dispatched_total", "Number of dispatches", statisticsHandler.getDispatched()),
                buildGauge("http_dispatched_active", "Number of dispatches currently active", statisticsHandler.getDispatchedActive()),
                buildGauge("http_dispatched_active_max", "Maximum number of active dispatches being handled", statisticsHandler.getDispatchedActiveMax()),
                buildGauge("http_dispatched_time_max", "Maximum time spent in dispatch handling", statisticsHandler.getDispatchedTimeMax()),
                buildGauge("http_dispatched_time_seconds_total", "Total time spent in dispatch handling", statisticsHandler.getDispatchedTimeTotal() / 1000.0),
                buildGauge("http_async_requests_total", "Total number of async requests", statisticsHandler.getAsyncRequests()),
                buildGauge("http_async_requests_waiting", "Currently waiting async requests", statisticsHandler.getAsyncRequestsWaiting()),
                buildGauge("http_async_requests_waiting_max", "Maximum number of waiting async requests", statisticsHandler.getAsyncRequestsWaitingMax()),
                buildGauge("http_async_dispatches_total", "Number of requested that have been asynchronously dispatched", statisticsHandler.getAsyncDispatches()),
                buildGauge("http_expires_total", "Number of async requests requests that have expired", statisticsHandler.getExpires()),
                buildStatusGauge(),
                buildGauge("http_stats_seconds", "Time in seconds stats have been collected for", statisticsHandler.getStatsOnMs() / 1000.0),
                buildGauge("http_responses_bytes_total", "Total number of bytes across all responses", statisticsHandler.getResponsesBytesTotal())
        );
    }

    private static MetricFamilySamples buildGauge(String name, String help, double value) {
        return new MetricFamilySamples(
                name,
                Type.GAUGE,
                help,
                singletonList(new MetricFamilySamples.Sample(name, emptyList(), emptyList(), value)));
    }

    private MetricFamilySamples buildStatusGauge() {
        String name = "http_responses";
        return new MetricFamilySamples(
                name,
                Type.GAUGE,
                "Number of requests with response status",
                asList(
                        buildStatusSample(name, "1xx", statisticsHandler.getResponses1xx()),
                        buildStatusSample(name, "2xx", statisticsHandler.getResponses2xx()),
                        buildStatusSample(name, "3xx", statisticsHandler.getResponses3xx()),
                        buildStatusSample(name, "4xx", statisticsHandler.getResponses4xx()),
                        buildStatusSample(name, "5xx", statisticsHandler.getResponses5xx())
                )
        );
    }

    private static MetricFamilySamples.Sample buildStatusSample(String name, String status, double value) {
        return new MetricFamilySamples.Sample(
                name,
                singletonList("code"),
                singletonList(status),
                value);
    }
}
