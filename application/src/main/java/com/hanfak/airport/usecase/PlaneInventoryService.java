package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
// tODO p1 split each method into individual interfaces
public interface PlaneInventoryService {
  void addPlane(Plane plane);
  void removePlane(Plane plane);
  boolean planeIsPresentInAirport(Plane plane);
}
