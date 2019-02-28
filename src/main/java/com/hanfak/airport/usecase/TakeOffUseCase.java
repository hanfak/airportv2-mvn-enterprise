package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneTakeOffStatus;

import static com.hanfak.airport.domain.PlaneTakeOffStatus.planeTakeOffStatus;

public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;

  public TakeOffUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }
  // Have extra field for failure reason in PlaneTakeOffStatus??
  // Or return tuple with new types SuccessfulPlaneTakeOffStatus &
  // FailurePlaneTakeOffStatus (includes reason for failure) ??
  // Can think about using railway programming??
  public PlaneTakeOffStatus instructPlaneToTakeOff(Plane plane) {
    if (planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      planeInventoryService.removePlane(plane);
      Plane flyingPlane = plane.fly();
      return planeTakeOffStatus(flyingPlane.planeId, flyingPlane.planeStatus, AirportStatus.NOT_IN_AIRPORT);
    }
    // Pass reason why it is not taken off
    return planeTakeOffStatus(plane.planeId, plane.planeStatus, AirportStatus.IN_AIRPORT);
  }
}
