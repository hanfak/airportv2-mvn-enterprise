package com.hanfak.airport.infrastructure.entrypoints.landplane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.infrastructure.entrypoints.JsonValidator;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import org.slf4j.Logger;

import static java.lang.String.format;

public class LandAirplaneWebservice {

  private final LandPlaneUseCase useCase;
  private final LandAirplaneRequestUnmarshaller unmarshaller;
  private final LandAirplaneResponseMarshaller marshaller;
  private final JsonValidator jsonValidator;
  private final Logger logger;

  public LandAirplaneWebservice(LandPlaneUseCase useCase, LandAirplaneRequestUnmarshaller unmarshaller, LandAirplaneResponseMarshaller marshaller, JsonValidator jsonValidator, Logger logger) {
    this.useCase = useCase;
    this.unmarshaller = unmarshaller;
    this.marshaller = marshaller;
    this.jsonValidator = jsonValidator;
    this.logger = logger;
  }

  // TODO test
  public RenderedContent execute(String request) throws JsonProcessingException {
    if (jsonValidator.validate(request).isPresent()) {
      logger.error(format("Request body is %s", request)); // The logging can be placed in the JsonValidator class
      logger.error("Exception", jsonValidator.validate(request).get());
      return new RenderedContent("Error with JSON Body in request", "text/plain", 500);
    }

    Plane plane = unmarshaller.unmarshal(request); // Optional<plane> to handle invalid json contents

    PlaneLandStatus planeLandStatus = useCase.instructPlaneToLand(plane);

    return marshallLandedPlaneStatus(planeLandStatus);
  }

  private RenderedContent marshallLandedPlaneStatus(PlaneLandStatus planeLandStatus) throws JsonProcessingException {
    if (planeLandStatus.failedPlaneLandStatus == null) {
      return marshaller.marshall(planeLandStatus.successfulPlaneLandStatus);
    } else {
      return marshaller.marshall(planeLandStatus.failedPlaneLandStatus);
    }
  }
}
