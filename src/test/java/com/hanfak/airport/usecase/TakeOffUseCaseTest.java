package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneTakeOffStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TakeOffUseCaseTest implements WithAssertions {

  @Test
  public void removesPlaneFromAirportWhenInstructToTakeOff() {
    when(hangerService.checkPlaneIsAtAirport(planeId("A0001"))).thenReturn(true);

    PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

    verify(hangerService).removePlane(plane);
    assertThat(actionUnderTest.successfulPlaneTakeOffStatus.airportStatus).isEqualTo(NOT_IN_AIRPORT);
    assertThat(actionUnderTest.successfulPlaneTakeOffStatus.planeStatus).isEqualTo(FLYING);
  }

  @Test
  public void doesNotRemovesPlaneFromAirportWhenInstructToTakeOff() {
    when(hangerService.checkPlaneIsAtAirport(planeId("A0001"))).thenReturn(false);

    PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

    verify(hangerService, never()).removePlane(plane);
    assertThat(actionUnderTest.failedPlaneTakeOffStatus.airportStatus).isEqualTo(IN_AIRPORT);
    assertThat(actionUnderTest.failedPlaneTakeOffStatus.planeStatus).isEqualTo(LANDED);
  }

  private final PlaneInventoryService hangerService = mock(PlaneInventoryService.class);
  private final TakeOffUseCase takeOffUseCase = new TakeOffUseCase(hangerService);
  private final Plane plane = new Plane(planeId("A0001"), LANDED);

}