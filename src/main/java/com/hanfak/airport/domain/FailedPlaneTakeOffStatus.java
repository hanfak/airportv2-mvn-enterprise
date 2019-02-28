package com.hanfak.airport.domain;
// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FailedPlaneTakeOffStatus extends ValueType {

  private final PlaneId planeId;
  private final PlaneStatus planeStatus;
  private final AirportStatus airportStatus;
  private final  String failureMessage; // turn to enum

  private FailedPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, String failureMessage) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
    this.failureMessage = failureMessage;
  }

  public static FailedPlaneTakeOffStatus failedPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, String failureMessage) {
    return new FailedPlaneTakeOffStatus(planeId, planeStatus, airportStatus, failureMessage);
  }
}
