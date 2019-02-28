package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.AirportStatus;
import com.hanfak.airport.domain.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.SuccessfulPlaneTakeOffStatus;
import javafx.util.Pair;

public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;

  public TakeOffUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }
  // Have extra field for failure reason in PlaneTakeOffStatus??
  // Or return tuple with new types SuccessfulPlaneTakeOffStatus &
  // FailurePlaneTakeOffStatus (includes reason for failure) ??
  // Throw exception for failure case??
  // Can think about using railway programming??
  public Pair<SuccessfulPlaneTakeOffStatus, FailedPlaneTakeOffStatus> instructPlaneToTakeOff(Plane plane) {
    if (planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      planeInventoryService.removePlane(plane);
      Plane flyingPlane = plane.fly();
      return new Pair<>(SuccessfulPlaneTakeOffStatus.planeTakeOffStatus(flyingPlane.planeId, flyingPlane.planeStatus, AirportStatus.NOT_IN_AIRPORT), null);
    }
    // Pass reason why it is not taken off
    return new Pair<>(null, FailedPlaneTakeOffStatus.planeTakeOffStatus(plane.planeId, plane.planeStatus, AirportStatus.IN_AIRPORT, "Blah"));
  }
}
