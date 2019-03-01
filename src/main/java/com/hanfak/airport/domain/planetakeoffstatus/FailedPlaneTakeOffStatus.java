package com.hanfak.airport.domain.planetakeoffstatus;

import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FailedPlaneTakeOffStatus extends ValueType {

  private final PlaneId planeId;
  private final PlaneStatus planeStatus;
  private final AirportStatus airportStatus;
  private final TakeOffFailureReason failureMessage;

  private FailedPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, TakeOffFailureReason failureMessage) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
    this.failureMessage = failureMessage;
  }

  public static FailedPlaneTakeOffStatus failedPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, TakeOffFailureReason failureMessage) {
    return new FailedPlaneTakeOffStatus(planeId, planeStatus, airportStatus, failureMessage);
  }
}
