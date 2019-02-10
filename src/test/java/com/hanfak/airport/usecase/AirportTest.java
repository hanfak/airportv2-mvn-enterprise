package com.hanfak.airport.usecase;


import com.hanfak.airport.domain.Plane;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;

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