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

public class TakeOffUseCaseTest implements WithAssertions {

  @Test
  public void removesPlaneFromAirportWhenInstructToTakeOff() {
    when(hangerService.checkPlaneIsAtAirport(planeId("A0001"))).thenReturn(true);

    boolean actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(landedPlane);

    verify(hangerService).removePlane(landedPlane);
    assertThat(actionUnderTest).isTrue();
  }

  @Test
  public void changesStatusOfPlaneAfterBeingRemovedFromHangerToFlying() {
  }

  private final PlaneInventoryService hangerService = mock(PlaneInventoryService.class);
  private final TakeOffUseCase takeOffUseCase = new TakeOffUseCase(hangerService);
  private final Plane flyingPlane = new Plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = new Plane(planeId("A0001"), LANDED);

}