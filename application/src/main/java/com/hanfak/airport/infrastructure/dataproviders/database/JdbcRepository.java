package com.hanfak.airport.infrastructure.dataproviders.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;

import java.util.Optional;

public interface JdbcRepository {
  Optional<Plane> read(PlaneId lookupId);

  void write(Plane plane);

  void delete(String lookupId);
}
