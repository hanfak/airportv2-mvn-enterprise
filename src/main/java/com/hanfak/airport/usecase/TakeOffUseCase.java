package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneTakeOffStatus;
import com.hanfak.airport.domain.SuccessfulPlaneTakeOffStatus;

import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.PlaneTakeOffStatus.createPlaneTakeOffStatus;
import static com.hanfak.airport.domain.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;

public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;

  public TakeOffUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }

  // Have extra field for failure reason in PlaneTakeOffStatus??
  // Or return tuple with new types SuccessfulPlaneTakeOffStatus &
  // FailurePlaneTakeOffStatus (includes reason for failure) ??
  // Throw exception for failure case?? but information will get lost
  // Use a map
  // Can think about using railway programming??
  public PlaneTakeOffStatus instructPlaneToTakeOff(Plane plane) {
    if (planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      planeInventoryService.removePlane(plane);
      Plane flyingPlane = plane.fly();
      SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(flyingPlane.planeId,
              flyingPlane.planeStatus,
              NOT_IN_AIRPORT);
      return createPlaneTakeOffStatus(successfulPlaneTakeOffStatus, null);
    }
    // Pass reason why it is not taken off
    FailedPlaneTakeOffStatus failedPlaneTakeOffStatus = failedPlaneTakeOffStatus(plane.planeId,
            plane.planeStatus,
            NOT_IN_AIRPORT,
            "Plane could not take off as it is not in the airport");
    return createPlaneTakeOffStatus(null, failedPlaneTakeOffStatus);
  }
}
