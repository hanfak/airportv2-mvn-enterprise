package com.hanfak.airport.domain;
// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class SuccessfulPlaneTakeOffStatus extends ValueType {

  private final PlaneId planeId;
  public final PlaneStatus planeStatus;
  private final AirportStatus airportStatus;

  private SuccessfulPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
  }

  public static SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    return new SuccessfulPlaneTakeOffStatus(planeId, planeStatus, airportStatus);
  }
}
