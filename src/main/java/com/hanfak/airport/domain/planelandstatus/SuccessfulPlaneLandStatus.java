package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class SuccessfulPlaneLandStatus extends ValueType {

  private final PlaneId planeId;
  public final PlaneStatus planeStatus;
  private final AirportStatus airportStatus;

  private SuccessfulPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
  }

  public static SuccessfulPlaneLandStatus successfulPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    return new SuccessfulPlaneLandStatus(planeId, planeStatus, airportStatus);
  }
}
