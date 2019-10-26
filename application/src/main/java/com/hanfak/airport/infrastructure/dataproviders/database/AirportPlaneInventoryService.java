package com.hanfak.airport.infrastructure.dataproviders.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.usecase.PlaneInventoryService;

public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final JdbcRepository airportStorageRepository;

  public AirportPlaneInventoryService(JdbcRepository airportStorageRepository) {
    this.airportStorageRepository = airportStorageRepository;
  }

  @Override
  public void addPlane(Plane plane) {
    airportStorageRepository.write(plane);
  }

  @Override
  public void removePlane(Plane plane) { // Should param be plane or planeId?
    airportStorageRepository.delete(plane.planeId.value);
  }

  @Override
  public boolean planeIsPresentInAirport(Plane plane) {
    return airportStorageRepository.read(plane.planeId).isPresent();
  }
}
