package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;
// New Usecase for airport controller to use, to assess the state of the plane by accessing the
// AirportHangerService using the planeId to get the its flight status

public class LandPlaneUseCase {

  private final PlaneInventoryService planeInventoryService;

  public LandPlaneUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }

  // What to return for application output, specific type to include plane, status, inAirport and inHanger (later specific hanger)
  // Pass in the plane ID, use a PlaneStatusService (a DB) to check if it is flying or landed???
  public boolean instructPlaneToLand(Plane plane) {
    if (LANDED.equals(plane.planeStatus)) {
      return false;
    }

    if (!planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      Plane landedPlane = plane.land();
      return planeInventoryService.addPlane(landedPlane);
    }

    return false;
  }
}
