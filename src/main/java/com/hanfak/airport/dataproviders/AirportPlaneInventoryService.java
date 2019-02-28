package com.hanfak.airport.dataproviders;

import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneId;
import com.hanfak.airport.usecase.PlaneInventoryService;

import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;
import static java.util.Collections.unmodifiableList;

public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final List<Plane> planesInventory = new ArrayList<>();

  @Override
  public List<Plane> planeInventory() {
    return unmodifiableList(planesInventory);
  }

  @Override
  public void addPlane(Plane plane) {
    // Throw exception if plane is in planesInventory
    if (!checkPlaneIsAtAirport(plane.planeId)) {
      addLandedPlaneToHanger(plane);
    }
  }

  @Override
  public void removePlane(Plane plane) { // Should param be plane or planeId?
    planesInventory.remove(plane);
  }

  @Override
  public Boolean checkPlaneIsAtAirport(PlaneId planeId) {
    return planesInventory.stream().anyMatch(plane -> planeId.equals(plane.planeId));
  }

  private boolean addLandedPlaneToHanger(Plane plane) {
    // Throw exception if plane is still flying
    return LANDED.equals(plane.planeStatus) && planesInventory.add(plane);
  }
}
