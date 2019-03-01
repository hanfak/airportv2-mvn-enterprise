package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason;

import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus.createPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;

public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;

  public TakeOffUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }

  // return tuple with new types SuccessfulPlaneTakeOffStatus &
  // FailurePlaneTakeOffStatus (includes reason for failure) ??
  // Or Throw exception for failure case?? but information will get lost
  // Or Use a map
  // Or Can think about using railway programming??
  public PlaneTakeOffStatus instructPlaneToTakeOff(Plane plane) {
    if (FLYING.equals(plane.planeStatus)) {
      return createPlaneTakeOffStatus(null,
              getFailedPlaneTakeOffStatus(plane, PLANE_IS_FLYING));
    }

    // Should this check be here or in planeInventoryService? if in planeInventoryService can throw exception
    if (planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      planeInventoryService.removePlane(plane);
      Plane flyingPlane = plane.fly();
      return createPlaneTakeOffStatus(getSuccessfulPlaneTakeOffStatus(flyingPlane), null);
    }

    return createPlaneTakeOffStatus(null,
            getFailedPlaneTakeOffStatus(plane, PLANE_IS_NOT_AT_THE_AIRPORT));
  }
// better name
  private SuccessfulPlaneTakeOffStatus getSuccessfulPlaneTakeOffStatus(Plane flyingPlane) {
    return successfulPlaneTakeOffStatus(flyingPlane.planeId, flyingPlane.planeStatus, NOT_IN_AIRPORT);
  }
// better name
  // reason should be constant in other class
  private FailedPlaneTakeOffStatus getFailedPlaneTakeOffStatus(Plane plane, TakeOffFailureReason reason) {
    return failedPlaneTakeOffStatus(plane.planeId, plane.planeStatus, NOT_IN_AIRPORT, reason);
  }
}
