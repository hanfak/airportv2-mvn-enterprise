package com.hanfak.airport;


import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.PlaneId.planeId;
import static com.hanfak.airport.PlaneStatus.FLYING;
import static com.hanfak.airport.PlaneStatus.LANDED;

public class AirportTest implements WithAssertions {

  @Test
  public void airportHasNoPlanesInHanger() {
    assertThat(airport.hanger).isEmpty();
  }

  @Test
  public void airportHasAPlaneWhenItInstructsPlaneToLand() {
    airport.instructPlaneToLand(flyingPlane);

    assertThat(airport.hanger.get(0).planeId).isEqualTo(landedPlane.planeId);
  }

  private final Airport airport = new Airport();
  private final Plane flyingPlane = new Plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = new Plane(planeId("A0001"), LANDED);
}