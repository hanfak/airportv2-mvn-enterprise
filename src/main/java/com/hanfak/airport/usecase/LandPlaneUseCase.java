package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;
// New Usecase for airport controller to use, to assess the state of the plane by accessing the
// AirportHangerService using the planeId to get the its flight status

public class LandPlaneUseCase {

  private final HangerService hangerService;

  public LandPlaneUseCase(HangerService hangerService) {
    this.hangerService = hangerService;
  }

  // What to return for application output, specific type to include plane, status, inAirport and inHanger (later specific hanger)
  // Pass in the plane ID, use a PlaneStatusService (a DB) to check if it is flying or landed???
  public boolean instructPlaneToLand(Plane plane) {
    if (plane.planeStatus.equals(LANDED)) {
      return false;
    }
    return hangerService.addPlane(new Plane(plane.planeId, LANDED));
  }
}
