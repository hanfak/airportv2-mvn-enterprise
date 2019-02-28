package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneId;

import java.util.List;

public interface PlaneInventoryService {
  List<Plane> planeInventory();
  boolean addPlane(Plane plane);
  boolean removePlane(Plane plane);

  Boolean checkPlaneIsAtAirport(PlaneId planeId);
}
