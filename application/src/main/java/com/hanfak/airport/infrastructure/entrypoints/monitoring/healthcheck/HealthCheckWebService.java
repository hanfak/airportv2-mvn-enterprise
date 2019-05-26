package com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck;

import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.HealthChecksUseCase;

public class HealthCheckWebService {

  private final HealthCheckResultMarshaller marshaller;
  private final HealthChecksUseCase healthChecksUseCase;

  public HealthCheckWebService(HealthCheckResultMarshaller marshaller, HealthChecksUseCase healthChecksUseCase) {
    this.marshaller = marshaller;
    this.healthChecksUseCase = healthChecksUseCase;
  }

  public RenderedContent getStatus() {
    return marshaller.marshal(healthChecksUseCase.getStatus());
  }
}
