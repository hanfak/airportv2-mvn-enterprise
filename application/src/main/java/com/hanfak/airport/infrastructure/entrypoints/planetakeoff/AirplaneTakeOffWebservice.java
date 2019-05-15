package com.hanfak.airport.infrastructure.entrypoints.planetakeoff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.TakeOffUseCase;

public class AirplaneTakeOffWebservice {

  private final TakeOffUseCase useCase;
  private final AirplaneTakeOffRequestUnmarshaller unmarshaller;
  private final AirplaneTakeOffResponseMarshaller marshaller;

  public AirplaneTakeOffWebservice(TakeOffUseCase useCase, AirplaneTakeOffRequestUnmarshaller unmarshaller, AirplaneTakeOffResponseMarshaller marshaller) {
    this.useCase = useCase;
    this.unmarshaller = unmarshaller;
    this.marshaller = marshaller;
  }

  // TODO: Apply validation to json and planeid like in LandAirplaneWebservice
  public RenderedContent execute(String request) throws JsonProcessingException {
    Plane plane = unmarshaller.unmarshal(request); // Invalid PlaneId will be handled by Jetty UncaughtErrorPage

    PlaneTakeOffStatus planeTakeOffStatus = useCase.instructPlaneToTakeOff(plane);

    if (planeTakeOffStatus.failedPlaneTakeOffStatus == null) {
      return marshaller.marshall(planeTakeOffStatus.successfulPlaneTakeOffStatus);
    } else {
      return marshaller.marshall(planeTakeOffStatus.failedPlaneTakeOffStatus);
    }
  }
}
