package com.hanfak.airport.infrastructure.entrypoints.landplane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;

import java.util.LinkedHashMap;
import java.util.Map;

public class LandAirplaneResponseMarshaller {

  public RenderedContent marshall(SuccessfulPlaneLandStatus successfulPlaneLandStatus) throws JsonProcessingException {
    return new RenderedContent(createSuccesfulResponseBodyJson(successfulPlaneLandStatus), "application/json", 200);
  }

  public RenderedContent marshall(FailedPlaneLandStatus failedPlaneLandStatus) throws JsonProcessingException {
    return new RenderedContent(createFailedResponseBodyJson(failedPlaneLandStatus), "application/json", 404);
  }

  private String createSuccesfulResponseBodyJson(SuccessfulPlaneLandStatus successfulPlaneLandStatus) throws JsonProcessingException {
    Map<String, String> jsonBody = new LinkedHashMap<>();
    jsonBody.put("PlaneId", successfulPlaneLandStatus.planeId.value);
    jsonBody.put("PlaneStatus", successfulPlaneLandStatus.planeStatus.name());
    jsonBody.put("AirportStatus", successfulPlaneLandStatus.airportStatus.name());

    return new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter())
            .writerWithDefaultPrettyPrinter().writeValueAsString(jsonBody);
  }

  private String createFailedResponseBodyJson(FailedPlaneLandStatus failedPlaneLandStatus) throws JsonProcessingException {
    Map<String, String> jsonBody = new LinkedHashMap<>();
    jsonBody.put("PlaneId", failedPlaneLandStatus.planeId.value);
    jsonBody.put("PlaneStatus", failedPlaneLandStatus.planeStatus.name());
    jsonBody.put("AirportStatus", failedPlaneLandStatus.airportStatus.name());
    jsonBody.put("LandFailureReason", failedPlaneLandStatus.failureMessage.toString());

    return new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter())
            .writerWithDefaultPrettyPrinter().writeValueAsString(jsonBody);
  }
}
