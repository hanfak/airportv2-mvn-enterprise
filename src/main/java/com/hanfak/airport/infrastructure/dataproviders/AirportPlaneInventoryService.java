package com.hanfak.airport.infrastructure.dataproviders;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageRepository;
import com.hanfak.airport.usecase.PlaneInventoryService;

// Split into two classes, one for dataprovider dependency, this to call dataprovider and check logic
public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final AirportStorageRepository airportStorageRepository;

  public AirportPlaneInventoryService(AirportStorageRepository airportStorageRepository) {
    this.airportStorageRepository = airportStorageRepository;
  }

  @Override
  public void addPlane(Plane plane) {
    if (!airportStorageRepository.read(plane.planeId).isPresent()) {
      airportStorageRepository.write(plane);
    } else {
      // Use custom one
      throw new IllegalStateException(String.format("Plane, '%s', in airport, cannot store plane in airport", plane.planeId));
    }
  }

  @Override
  public void removePlane(Plane plane) { // Should param be plane or planeId?
    if (airportStorageRepository.read(plane.planeId).isPresent()) {
      airportStorageRepository.delete(plane.planeId.value);
    } else {
      // Use custom one
      throw new IllegalStateException(String.format("Plane, '%s', not in airport, cannot remove plane", plane.planeId));
    }
  }
}
