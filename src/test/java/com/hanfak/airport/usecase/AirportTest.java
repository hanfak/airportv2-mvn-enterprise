package com.hanfak.airport.usecase;


import com.hanfak.airport.domain.Plane;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
  public void airportCannotInstructPlaneToLandIfPlaneIsInTheHanger() {
    when(hangerService.planeInventory()).thenReturn(singletonList(landedPlane));

    boolean actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(hangerService, never()).addPlane(any());
    assertThat(actionUnderTest).isFalse();
  }

  @Test
  public void planeIsConfirmedAsLandedWhenPlaneIsNotFlying() {
    boolean actionUnderTest = airport.instructPlaneToLand(landedPlane);

    assertThat(actionUnderTest).isFalse();

//    assertThat(airport.hangerService.get(0).planeId).isEqualTo(planeId("A0001"));
//    assertThat(airport.hangerService.get(0).planeStatus).isEqualTo(LANDED);
  }

  private final HangerService hangerService = mock(HangerService.class);
  private final Airport airport = new Airport(hangerService);
  private final Plane flyingPlane = new Plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = new Plane(planeId("A0001"), LANDED);
}