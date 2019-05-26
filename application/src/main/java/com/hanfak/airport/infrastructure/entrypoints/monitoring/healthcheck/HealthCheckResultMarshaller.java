package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;

import com.hanfak.airport.domain.helper.HealthCheckResult;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;

public class HealthCheckResultMarshaller {

    private static final int INDENT_FACTOR = 4;

    private final HealthCheckResultJsonBuilder statusResultBuilder;

    public HealthCheckResultMarshaller(HealthCheckResultJsonBuilder statusResultBuilder) {
        this.statusResultBuilder = statusResultBuilder;
    }

    RenderedContent marshal(HealthCheckResult healthCheckResult) {
        String body = statusResultBuilder.build(healthCheckResult).toString(INDENT_FACTOR);
        return new RenderedContent(body, "application/json", 200);
    }
}
