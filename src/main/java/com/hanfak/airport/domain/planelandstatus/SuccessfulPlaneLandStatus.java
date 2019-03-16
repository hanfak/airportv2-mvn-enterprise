package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class SuccessfulPlaneLandStatus extends ValueType {


  private SuccessfulPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
  }

  public static SuccessfulPlaneLandStatus successfulPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    return new SuccessfulPlaneLandStatus(planeId, planeStatus, airportStatus);
  }
}
