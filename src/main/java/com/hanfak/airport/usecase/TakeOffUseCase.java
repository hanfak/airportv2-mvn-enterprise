package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

public class TakeOffUseCase {

  private final HangerService hangerService;

  public TakeOffUseCase(HangerService hangerService) {
    this.hangerService = hangerService;
  }

  // What to return??? type - id, status, inHanger, inAirport,
  public boolean instructPlaneToTakeOff(Plane plane) {
    return hangerService.removePlane(plane);
  }
}
