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

public class LandPlaneUseCaseTest implements WithAssertions {

  @Test
  public void airportInstructsPlaneToLand() {
    when(planeInventoryService.checkPlaneIsAtAirport(flyingPlane.planeId)).thenReturn(false);

    boolean actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(planeInventoryService).addPlane(landedPlane);
    assertThat(actionUnderTest).isTrue();
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsNotFlying() {
    boolean actionUnderTest = airport.instructPlaneToLand(landedPlane);

    assertThat(actionUnderTest).isFalse();
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsAtTheAirport() {
    when(planeInventoryService.checkPlaneIsAtAirport(flyingPlane.planeId)).thenReturn(true);

    boolean actionUnderTest = airport.instructPlaneToLand(landedPlane);

    assertThat(actionUnderTest).isFalse();
  }



  private final PlaneInventoryService planeInventoryService = mock(PlaneInventoryService.class);
  private final LandPlaneUseCase airport = new LandPlaneUseCase(planeInventoryService);
  private final Plane flyingPlane = new Plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = new Plane(planeId("A0001"), LANDED);
}