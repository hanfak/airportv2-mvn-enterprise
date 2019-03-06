package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
// New Usecase for airport controller to use, to assess the state of the plane by accessing the
// AirportHangerService using the planeId to get the its flight status

public class LandPlaneUseCase {

  private final PlaneInventoryService planeInventoryService;

  public LandPlaneUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }

  // What to return for application output, specific type to include plane, status, inAirport and inHanger (later specific hanger)
  public PlaneLandStatus instructPlaneToLand(Plane plane) {
    if (LANDED.equals(plane.planeStatus)) {
      return null;
    }

    // Should this check be here or in planeInventoryService? if in planeInventoryService can throw exception
    if (!planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      Plane landedPlane = plane.land();
      planeInventoryService.addPlane(landedPlane);
      return getSuccessfulPlaneLandStatus(landedPlane);
    }

    return null;
  }

  private PlaneLandStatus getSuccessfulPlaneLandStatus(Plane landedPlane) {
    return PlaneLandStatus.createPlaneLandStatus( SuccessfulPlaneLandStatus.successfulPlaneLandStatus(landedPlane.planeId, landedPlane.planeStatus, IN_AIRPORT)
, null);
  }
}
