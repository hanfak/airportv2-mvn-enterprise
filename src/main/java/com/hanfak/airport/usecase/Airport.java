package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;

// Usecase
public class Airport {

  private final HangerService hangerService;

  public Airport(HangerService hangerService) {
    this.hangerService = hangerService;
  }

  // What to return for application output, specific type
  public boolean instructPlaneToLand(Plane plane) {
    Plane landedPlane = new Plane(plane.planeId, LANDED);
    if (plane.planeStatus.equals(LANDED)) {
      return false;
    }
    return !hangerService.planeInventory().contains(landedPlane) && hangerService.addPlane(landedPlane);
  }
}
