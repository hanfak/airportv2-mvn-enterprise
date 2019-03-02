package com.hanfak.airport.infrastructure.dataproviders;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.usecase.PlaneInventoryService;

import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static java.util.Collections.unmodifiableList;

// Split into two classes, one for dataprovider dependency, this to call dataprovider and check logic
public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final List<Plane> planesInventory = new ArrayList<>();

  @Override
  public List<Plane> planeInventory() {
    return unmodifiableList(planesInventory);
  }

  @Override
  public void addPlane(Plane plane) {
    // Throw exception if plane is in airport
    if (!checkPlaneIsAtAirport(plane.planeId)) {
      addLandedPlaneToHanger(plane);
    }
  }

  @Override
  public void removePlane(Plane plane) { // Should param be plane or planeId?
    if (checkPlaneIsAtAirport(plane.planeId)) {
      planesInventory.remove(plane);
    } else {
      // Use custom one
      throw new IllegalStateException(String.format("Plane, '%s', not in airport, cannot remove plane", plane.planeId));
    }
  }

  @Override
  public Boolean checkPlaneIsAtAirport(PlaneId planeId) {
    return planesInventory.stream().anyMatch(plane -> planeId.equals(plane.planeId));
  }

  private void addLandedPlaneToHanger(Plane plane) {
    if (LANDED.equals(plane.planeStatus)) { // Do we need this check if done in use case??
      planesInventory.add(plane);
    }
  }
}