package com.hanfak.airport.infrastructure.entrypoints.planetakeoff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AirplaneTakeOffResponseMarshaller {

  public RenderedContent marshall(SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus) throws JsonProcessingException {
    return new RenderedContent(createSuccessfulResponseBodyJson(successfulPlaneTakeOffStatus), "application/json", 200);
  }

  public RenderedContent marshall(FailedPlaneTakeOffStatus failedPlaneTakeOffStatus) throws JsonProcessingException {
    return new RenderedContent(createFailedResponseBodyJson(failedPlaneTakeOffStatus), "application/json", 404);
  }

  private String createSuccessfulResponseBodyJson(SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus) throws JsonProcessingException {
    HashMap<String, String> jsonBody = new LinkedHashMap<>();
    jsonBody.put("PlaneId", successfulPlaneTakeOffStatus.planeId.value);
    jsonBody.put("PlaneStatus", successfulPlaneTakeOffStatus.planeStatus.name());
    jsonBody.put("AirportStatus", successfulPlaneTakeOffStatus.airportStatus.name());

    return new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter())
            .writerWithDefaultPrettyPrinter().writeValueAsString(jsonBody);
  }

  private String createFailedResponseBodyJson(FailedPlaneTakeOffStatus failedPlaneTakeOffStatus) throws JsonProcessingException {
    HashMap<String, String> jsonBody = new LinkedHashMap<>();
    jsonBody.put("PlaneId", failedPlaneTakeOffStatus.planeId.value);
    jsonBody.put("PlaneStatus", failedPlaneTakeOffStatus.planeStatus.name());
    jsonBody.put("AirportStatus", failedPlaneTakeOffStatus.airportStatus.name());
    jsonBody.put("TakeOffFailureReason", failedPlaneTakeOffStatus.failureMessage.toString());

    return new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter())
            .writerWithDefaultPrettyPrinter().writeValueAsString(jsonBody);
  }
}
