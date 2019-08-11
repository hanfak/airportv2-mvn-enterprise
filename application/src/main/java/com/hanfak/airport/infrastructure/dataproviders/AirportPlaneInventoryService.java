package com.hanfak.airport.infrastructure.dataproviders;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import com.hanfak.airport.usecase.PlaneInventoryService;

// Split into two classes, one for dataprovider dependency, this to call dataprovider and check logic
public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final AirportStorageJdbcRepository airportStorageRepository;

  public AirportPlaneInventoryService(AirportStorageJdbcRepository airportStorageRepository) {
    this.airportStorageRepository = airportStorageRepository;
  }
  /**
      TODO:
      instead of addPlane checking plane is here, upstream should do the check and avoid using
      exception to dictate the flow of the app

      OR

      return true/false dependent on success

      But from business prospective,
          if a plane trying to land is in airport, thus plane is using wrong id or fake
              or user of app passed in wrong input
          thus exceptionally

   */
  @Override
  public void addPlane(Plane plane) {
    if (airportStorageRepository.read(plane.planeId).isPresent()) {
      // Use custom one
      throw new IllegalStateException(String.format("Plane, '%s', in airport, cannot store plane in airport", plane.planeId));
    } else {
      airportStorageRepository.write(plane);
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
