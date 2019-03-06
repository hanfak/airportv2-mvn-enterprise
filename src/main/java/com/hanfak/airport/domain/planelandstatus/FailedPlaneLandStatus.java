package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FailedPlaneLandStatus extends ValueType {

  private final PlaneId planeId;
  private final PlaneStatus planeStatus;
  private final AirportStatus airportStatus;
  private final TakeOffFailureReason failureMessage;

  private FailedPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, TakeOffFailureReason failureMessage) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
    this.failureMessage = failureMessage;
  }

  public static FailedPlaneLandStatus failedPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, TakeOffFailureReason failureMessage) {
    return new FailedPlaneLandStatus(planeId, planeStatus, airportStatus, failureMessage);
  }
}
