package com.hanfak.airport.infrastructure.dataproviders;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import com.hanfak.airport.usecase.PlaneInventoryService;

// TODO move to dtabase package
public class AirportPlaneInventoryService implements PlaneInventoryService {

  private final AirportStorageJdbcRepository airportStorageRepository;

  public AirportPlaneInventoryService(AirportStorageJdbcRepository airportStorageRepository) {
    this.airportStorageRepository = airportStorageRepository;
  }

  /**
   * TODO:
   * from business prospective,
   * if a plane trying to land is in airport, thus plane is using wrong id or fake
   * or user of app passed in wrong input
   * thus exceptionally
   */
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
