package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused", "PMD.UncommentedEmptyConstructor"})
public class FailedPlaneLandStatus extends ValueType {


  private final PlaneId planeId;
  private final PlaneStatus planeStatus;
  private final AirportStatus airportStatus;
  private final LandFailureReason failureMessage;

  public FailedPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, LandFailureReason failureMessage) {
    this.planeId = planeId;
    this.planeStatus = planeStatus;
    this.airportStatus = airportStatus;
    this.failureMessage = failureMessage;
  }

  public static FailedPlaneLandStatus failedPlaneLandStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus, LandFailureReason failureMessage) {
    return new FailedPlaneLandStatus(planeId, planeStatus, airportStatus, failureMessage);
  }
}
