package com.hanfak.airport.infrastructure.entrypoints.landplane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.plane.IllegalCharacterException;
import com.hanfak.airport.domain.plane.IllegalLengthException;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.infrastructure.entrypoints.JsonValidator;
import com.hanfak.airport.infrastructure.entrypoints.RequestUnmarshaller;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
    this.jsonValidator = jsonValidator;
    this.logger = logger;
  }

  public RenderedContent execute(String request) throws JsonProcessingException {
    if (jsonValidator.checkForInvalidJson(request).isPresent()) {
      return createRenderedContentForInvalidJson(request);
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

  private RenderedContent createRenderedContentForRequestContent(String body, String request, IllegalArgumentException exception) {
    logger.error(format("Error Message: '%s'\nRequest body is '%s'", body, request), exception);
    Map<String, String> headers = new HashMap<>();
    headers.put("Retriable", "true");
    return new RenderedContent(body, "text/plain", 500, headers);
  }

  private RenderedContent createRenderedContentForInvalidJson(String request) {
    logger.error(format("Request body is '%s'", request), jsonValidator.checkForInvalidJson(request).get());
    Map<String, String> headers = new HashMap<>();
    headers.put("Retriable", "true");
    return new RenderedContent("Error with JSON Body in request", "text/plain", 500, headers);
  }

  private RenderedContent marshallLandedPlaneStatus(PlaneLandStatus planeLandStatus) throws JsonProcessingException {
    if (planeLandStatus.failedPlaneLandStatus == null) {
      return marshaller.marshall(planeLandStatus.successfulPlaneLandStatus);
    } else {
      // TODO P1 retriable error status code (503) and header for system error
      //https://stackoverflow.com/questions/17862015/http-statuscode-to-retry-same-request
      //https://stackoverflow.com/questions/9794696/how-do-i-choose-a-http-status-code-in-rest-api-for-not-ready-yet-try-again-lat?noredirect=1&lq=1
      return marshaller.marshall(planeLandStatus.failedPlaneLandStatus);
    }
  }
}
