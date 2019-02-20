package com.hanfak.airport.usecase;


import com.hanfak.airport.domain.Plane;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AirportTest implements WithAssertions {

  @Test
  public void airportInstructsPlaneToLand() {
    when(hangerService.addPlane(landedPlane)).thenReturn(true);

    boolean actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(hangerService).addPlane(landedPlane);
    assertThat(actionUnderTest).isTrue();
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsNotFlying() {
    boolean actionUnderTest = airport.instructPlaneToLand(landedPlane);

    assertThat(actionUnderTest).isFalse();


  }

  private final HangerService hangerService = mock(HangerService.class);
  private final Airport airport = new Airport(hangerService);
  private final Plane flyingPlane = new Plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = new Plane(planeId("A0001"), LANDED);
}