package com.hanfak.airport.domain;
// Better name
public class SuccessfulPlaneTakeOffStatus extends ValueType {

  private final PlaneId planeId;
  public final PlaneStatus planeStatus;
  public final AirportStatus airportStatus;

  // make into static factory method
  private SuccessfulPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
  }

  public static SuccessfulPlaneTakeOffStatus planeTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    return new SuccessfulPlaneTakeOffStatus(planeId, planeStatus, airportStatus);
  }
}
