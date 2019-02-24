package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;
// New Usecase for airport controller to use, to assess the state of the plane by accessing the
// AirportHangerService using the planeId to get the its flight status

// Usecase
public class Airport {

  private final HangerService hangerService;

  public Airport(HangerService hangerService) {
    this.hangerService = hangerService;
  }

  // What to return for application output, specific type
  public boolean instructPlaneToLand(Plane plane) {
    if (plane.planeStatus.equals(LANDED)) {
      return false;
    }
    return hangerService.addPlane(new Plane(plane.planeId, LANDED));
  }
// Split into different classes
  // What to return???
  public boolean instructPlaneToTakeOff(Plane plane) {
    return hangerService.removePlane(plane);
  }
}
