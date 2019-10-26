package com.hanfak.airport.infrastructure.entrypoints.landplane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.plane.IllegalCharacterException;
import com.hanfak.airport.domain.plane.IllegalLengthException;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.infrastructure.crosscutting.JsonValidator;
import com.hanfak.airport.infrastructure.entrypoints.RequestUnmarshaller;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_COULD_NOT_LAND;
import static java.lang.String.format;

public class LandAirplaneWebservice {

  private final LandPlaneUseCase useCase;
  private final RequestUnmarshaller unmarshaller;
  private final LandAirplaneResponseMarshaller marshaller;
  private final JsonValidator jsonValidator;
  private final Logger logger;

  public LandAirplaneWebservice(LandPlaneUseCase useCase, RequestUnmarshaller unmarshaller, LandAirplaneResponseMarshaller marshaller, JsonValidator jsonValidator, Logger logger) {
    this.useCase = useCase;
    this.unmarshaller = unmarshaller;
    this.marshaller = marshaller;
    this.jsonValidator = jsonValidator; // TODO P1: should happen in unmarshaller
    this.logger = logger;
  }

  public RenderedContent execute(String request) throws JsonProcessingException {
    Optional<IOException> invalidJsonException = jsonValidator.checkForInvalidJson(request);
    if (invalidJsonException.isPresent()) {
      return createRenderedContentForInvalidJson(request, invalidJsonException.get());
    }

    try {
      Plane plane = unmarshaller.unmarshal(request); // Optional<plane> to handle invalid json contents

      PlaneLandStatus planeLandStatus = useCase.instructPlaneToLand(plane);

      return marshallLandedPlaneStatus(planeLandStatus);
    } catch (IllegalCharacterException e) {
      return createRenderedContentForRequestContent("Error with content in body of request: planeId contains illegal character", request, e);
    } catch (IllegalLengthException e) {
      return createRenderedContentForRequestContent("Error with content in body of request: planeId is wrong length", request, e);
    } // Catch runtime and set as technical failure
  }

  private RenderedContent createRenderedContentForInvalidJson(String request, IOException exception) {
    logger.error(format("Request body is '%s'", request), exception);
    Map<String, String> headers = new HashMap<>();
    headers.put("Retriable", "true"); // TODO should not be retriable as will do same request over again, with out changing request
    return new RenderedContent("Error with JSON Body in request", "text/plain", 500, headers);
  }

  private RenderedContent createRenderedContentForRequestContent(String body, String request, IllegalArgumentException exception) {
    logger.error(format("Error Message: '%s'\nRequest body is '%s'", body, request), exception);
    Map<String, String> headers = new HashMap<>();
    headers.put("Retriable", "true"); // TODO should not be retriable as will do same request over again, with out changing request
    return new RenderedContent(body, "text/plain", 500, headers);
  }

  private RenderedContent marshallLandedPlaneStatus(PlaneLandStatus planeLandStatus) throws JsonProcessingException {
    FailedPlaneLandStatus failedPlaneLandStatus = planeLandStatus.failedPlaneLandStatus;
    if (failedPlaneLandStatus == null) {
      return marshaller.marshall(planeLandStatus.successfulPlaneLandStatus);
    } else {
      // TODO P1: also retriale for weather retrievable
      if (PLANE_COULD_NOT_LAND.equals(failedPlaneLandStatus.failureMessage)) {
        return marshaller.marshallRetriableFailure(failedPlaneLandStatus);
      }
      return marshaller.marshall(failedPlaneLandStatus);
    }
  }
}
