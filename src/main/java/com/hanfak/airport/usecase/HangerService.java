package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;

import java.util.List;

public interface HangerService {
  List<Plane> planeInventory();
  boolean addPlane(Plane plane);
  boolean removePlane(Plane plane);
}
