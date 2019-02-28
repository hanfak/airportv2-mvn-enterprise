package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

public class TakeOffUseCase {

  private final PlaneInventoryService planeInventoryService;

  public TakeOffUseCase(PlaneInventoryService hangerService) {
    this.planeInventoryService = hangerService;
  }

  // What to return??? type - id, status, inHanger, inAirport,
  public boolean instructPlaneToTakeOff(Plane plane) {
    if (planeInventoryService.checkPlaneIsAtAirport(plane.planeId)) {
      planeInventoryService.removePlane(plane);
      return true; // replace booleans with call to planeInventoryService.checkPlaneIsAtAirport(plane.planeId)
    }
    return false;
  }
}
