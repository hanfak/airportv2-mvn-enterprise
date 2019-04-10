package com.hanfak.airport.domain.planelandstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused", "PMD.SingularField", "URF_UNREAD_FIELD"})
public class FailedPlaneLandStatus extends ValueType {

  public final PlaneId planeId;
  public final PlaneStatus planeStatus;
  public final AirportStatus airportStatus;
  public final LandFailureReason failureMessage;

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
