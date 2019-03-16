package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FailedPlaneLandStatus extends ValueType {

  public FailedPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, LandFailureReason failureMessage) {
  }

  public static FailedPlaneLandStatus failedPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, LandFailureReason failureMessage) {
    return new FailedPlaneLandStatus(planeId, planeStatus, airportStatus, failureMessage);
  }
}
