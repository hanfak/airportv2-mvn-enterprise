package com.hanfak.airport.domain;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;

public class PlaneTest implements WithAssertions {

  @Test
  public void planeHasLanded() {
    Plane plane = new Plane(planeId("A0001"), FLYING);

    Plane landedPlane = plane.land();

    assertThat(landedPlane.planeStatus).isEqualTo(LANDED);
  }

  @Test
  public void planeIsFlying() {
    Plane plane = new Plane(planeId("A0001"), LANDED);

    Plane landedPlane = plane.fly();

    assertThat(landedPlane.planeStatus).isEqualTo(FLYING);
  }
}