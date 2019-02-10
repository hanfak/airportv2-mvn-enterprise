package com.hanfak.airport;

import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.PlaneStatus.LANDED;

// Usecase
public class Airport {

  public final List<Plane> hanger = new ArrayList<>(); // separate service

  // What to return???
  public boolean instructPlaneToLand(Plane plane) {
    hanger.add(new Plane(plane.planeId, LANDED));
    return true;
  }
}
