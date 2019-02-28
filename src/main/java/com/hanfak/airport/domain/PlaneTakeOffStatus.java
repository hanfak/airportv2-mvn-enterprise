package com.hanfak.airport.domain;
// Better name
public class PlaneTakeOffStatus extends ValueType {

  private final PlaneId planeId;
  public final PlaneStatus planeStatus;
  public final AirportStatus airportStatus;

  // make into static factory method
  private PlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
  }

  public static PlaneTakeOffStatus planeTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    return new PlaneTakeOffStatus(planeId, planeStatus, airportStatus);
  }
}
