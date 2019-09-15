package com.hanfak.airport.infrastructure.dataproviders.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import com.hanfak.airport.usecase.PlaneInventoryService;

public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final AirportStorageJdbcRepository airportStorageRepository;

  public AirportPlaneInventoryService(AirportStorageJdbcRepository airportStorageRepository) {
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
