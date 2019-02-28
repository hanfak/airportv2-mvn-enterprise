package com.hanfak.airport.dataproviders;

import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.usecase.PlaneInventoryService;

import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;

public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final List<Plane> hanger = new ArrayList<>();

  @Override // Better name
  public List<Plane> planeInventory() {
    return hanger; // Use copy or turn into immodifiable  list
  }
  // This is a command, and should not return something and act as a query, thus have multiple responsibilities ie CQRS
  @Override
  public boolean addPlane(Plane plane) {
    // Throw exception if plane is in hanger
    return !hanger.contains(plane) && addLandedPlaneToHanger(plane);
  }

  // This is a command, and should not return something and act as a query, thus have multiple responsibilities ie CQRS
  @Override
  public boolean removePlane(Plane plane) { // Should param be plane or planeId?
    return hanger.remove(plane);
  }

  private boolean addLandedPlaneToHanger(Plane plane) {
    // Throw exception if plane is still flying
    return LANDED.equals(plane.planeStatus) && hanger.add(plane);
  }
}
