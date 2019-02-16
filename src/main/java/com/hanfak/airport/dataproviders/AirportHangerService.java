package com.hanfak.airport.dataproviders;

import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.usecase.HangerService;

import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.domain.PlaneStatus.LANDED;

public class AirportHangerService implements HangerService {

  public final List<Plane> hanger = new ArrayList<>();

  @Override // Better name
  public List<Plane> planeInventory() {
    return hanger; // Use copy or turn into immodifiable  list
  }

  @Override
  public boolean addPlane(Plane plane) {
    return !hanger.contains(plane) && addLandedPlaneToHanger(plane);
  }

  private boolean addLandedPlaneToHanger(Plane plane) {
    return LANDED.equals(plane.planeStatus) && hanger.add(plane);
  }
}
