package com.hanfak.airport.domain.planetakeoffstatus;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.helper.ValueType;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

// Better name
@SuppressWarnings({"FieldCanBeLocal", "unused"})
@SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
public class SuccessfulPlaneTakeOffStatus extends ValueType {

  public final PlaneStatus planeStatus;

  private SuccessfulPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    this.planeStatus = planeStatus;
  }

  public static SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus(PlaneId planeId, PlaneStatus planeStatus, AirportStatus airportStatus) {
    return new SuccessfulPlaneTakeOffStatus(planeId, planeStatus, airportStatus);
  }
}
