package com.hanfak.airport.infrastructure.entrypoints;

import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import org.json.JSONObject;

public class LandAirplaneResponseMarshaller {

  public RenderedContent marshall(SuccessfulPlaneLandStatus successfulPlaneLandStatus) {
    return new RenderedContent(createInnerSuccesfulResponseJson(successfulPlaneLandStatus), "application/json", 200);
  }

  public RenderedContent marshall(FailedPlaneLandStatus failedPlaneLandStatus) {
    return new RenderedContent(createInnerFailedResponseJson(failedPlaneLandStatus), "application/json", 404);
  }

  private String createInnerSuccesfulResponseJson(SuccessfulPlaneLandStatus successfulPlaneLandStatus) {
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("PlaneId", successfulPlaneLandStatus.planeId);
    jsonObj.put("PlaneStatus", successfulPlaneLandStatus.planeStatus);
    jsonObj.put("AirportStatus", successfulPlaneLandStatus.airportStatus);
    return jsonObj.toString(4);
  }

  private String createInnerFailedResponseJson(FailedPlaneLandStatus failedPlaneLandStatus) {
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("PlaneId", failedPlaneLandStatus.planeId);
    jsonObj.put("PlaneStatus", failedPlaneLandStatus.planeStatus);
    jsonObj.put("AirportStatus", failedPlaneLandStatus.airportStatus);
    jsonObj.put("LandFailureReason", failedPlaneLandStatus.failureMessage);
    return jsonObj.toString(4);
  }
}
