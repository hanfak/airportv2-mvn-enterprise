package com.hanfak.airport.infrastructure.entrypoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.LandPlaneUseCase;

public class LandAirplaneWebservice {

  private final LandPlaneUseCase useCase;
  private final LandAirplaneRequestUnmarshaller unmarshaller;
  private final LandAirplaneResponseMarshaller marshaller;

  public LandAirplaneWebservice(LandPlaneUseCase useCase, LandAirplaneRequestUnmarshaller unmarshaller, LandAirplaneResponseMarshaller marshaller) {
    this.useCase = useCase;
    this.unmarshaller = unmarshaller;
    this.marshaller = marshaller;
  }

  public RenderedContent execute(String request) throws JsonProcessingException {
    Plane plane = unmarshaller.unmarshal(request);

    PlaneLandStatus planeLandStatus = useCase.instructPlaneToLand(plane);

    if (planeLandStatus.failedPlaneLandStatus == null) {
      return marshaller.marshall(planeLandStatus.successfulPlaneLandStatus);
    } else {
      return marshaller.marshall(planeLandStatus.failedPlaneLandStatus);
    }
  }
}
