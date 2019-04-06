package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;

public interface PlaneInventoryService {
  void addPlane(Plane plane);
  void removePlane(Plane plane);
}
