package com.hanfak.airport.domain.plane;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;

public class PlaneTest implements WithAssertions {

  @Test
  public void planeHasLanded() {
    Plane plane = plane(planeId("A0001"), FLYING);

    Plane landedPlane = plane.land();

    assertThat(landedPlane.planeStatus).isEqualTo(LANDED);
  }

  @Test
  public void planeIsFlying() {
    Plane plane = plane(planeId("A0001"), LANDED);

    Plane landedPlane = plane.fly();

    assertThat(landedPlane.planeStatus).isEqualTo(FLYING);
  }

  @Test
  public void validatePlaneIdIsNotNull() {
    assertThatThrownBy(() -> plane(null, LANDED))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Field is null");
  }

  @Test
  public void validatePlaneStatusIsNotNull() {
    assertThatThrownBy(() -> plane(planeId("A0001"), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Field is null");
  }
}