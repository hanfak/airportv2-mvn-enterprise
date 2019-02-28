package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.*;

import static com.hanfak.airport.domain.PlaneTakeOffStatus.createPlaneTakeOffStatus;

public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;

  public TakeOffUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }
  // Have extra field for failure reason in PlaneTakeOffStatus??
  // Or return tuple with new types SuccessfulPlaneTakeOffStatus &
  // FailurePlaneTakeOffStatus (includes reason for failure) ??
  // Throw exception for failure case??
  // encapsulate both types in one object
  // Use a map
  // Can think about using railway programming??
  public PlaneTakeOffStatus instructPlaneToTakeOff(Plane plane) {
    if (planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      planeInventoryService.removePlane(plane);
      Plane flyingPlane = plane.fly();
      return createPlaneTakeOffStatus(SuccessfulPlaneTakeOffStatus.planeTakeOffStatus(flyingPlane.planeId, flyingPlane.planeStatus, AirportStatus.NOT_IN_AIRPORT), null);
    }
    // Pass reason why it is not taken off
    return createPlaneTakeOffStatus(null, FailedPlaneTakeOffStatus.planeTakeOffStatus(plane.planeId, plane.planeStatus, AirportStatus.IN_AIRPORT, "Blah"));
  }
}
