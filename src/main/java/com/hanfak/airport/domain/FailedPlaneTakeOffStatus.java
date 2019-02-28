package com.hanfak.airport.domain;
// Better name
public class FailedPlaneTakeOffStatus extends ValueType {

  private final PlaneId planeId;
  public final PlaneStatus planeStatus;
  public final AirportStatus airportStatus;
  private final  String failureMessage; // turn to enum

  // make into static factory method
  private FailedPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, String failureMessage) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
    this.failureMessage = failureMessage;
  }

  public static FailedPlaneTakeOffStatus planeTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, String failureMessage) {
    return new FailedPlaneTakeOffStatus(planeId, planeStatus, airportStatus, failureMessage);
  }
}
