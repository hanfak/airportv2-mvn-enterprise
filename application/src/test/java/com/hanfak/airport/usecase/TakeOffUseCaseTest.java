package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_COULD_NOT_TAKE_OFF;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TakeOffUseCaseTest implements WithAssertions {

    @Test
    public void removesPlaneFromAirportWhenInstructToTakeOff() {
        when(hangerService.planeIsPresentInAirport(plane)).thenReturn(true);
        PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

        verify(hangerService).removePlane(plane);
        verify(logger).info(eq("Plane, 'A0001', has successfully left the airport"));
        assertThat(actionUnderTest.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
    }

    @Test
    public void planeDoesNotLeaveAirportWhenPlaneIsFlying() {
        PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(flyingPlane);

        verify(hangerService, never()).removePlane(plane);
        verify(logger).info("Plane, 'A0001', cannot take off, status is 'FLYING', it is not at the airport");
        assertThat(actionUnderTest.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForFlyingPlane);
    }

    @Test
    public void planeCannotTakeOffWhenErrorWithSystem() {
        when(hangerService.planeIsPresentInAirport(plane)).thenReturn(true);
        IllegalStateException cause = new IllegalStateException("Blah");
        doThrow(cause).when(hangerService).removePlane(plane);
        PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

        verify(logger).error("Something went wrong removing the Plane, 'A0001', at the airport", cause);
        assertThat(actionUnderTest.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForErrorInSystem);
    }

    @Test
    public void cannotTakeOffWhenSystemErrorForCheckingPlaneInAirport() {
        IllegalStateException cause = new IllegalStateException("Blah");
        doThrow(cause).when(hangerService).planeIsPresentInAirport(plane);
        PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

        verify(logger).error("Something went wrong removing the Plane, 'A0001', at the airport", cause);
        assertThat(actionUnderTest.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForErrorInSystem);
    }

    private final PlaneInventoryService hangerService = mock(PlaneInventoryService.class);
    private final Logger logger = mock(Logger.class);

    private final TakeOffUseCase takeOffUseCase = new TakeOffUseCase(hangerService, logger);
    private final Plane plane = plane(planeId("A0001"), LANDED);
    private final Plane flyingPlane = plane(planeId("A0001"), FLYING);
    private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForFlyingPlane = failedPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, PLANE_IS_FLYING);
    private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForErrorInSystem = failedPlaneTakeOffStatus(planeId("A0001"), LANDED, IN_AIRPORT, PLANE_COULD_NOT_TAKE_OFF);
    private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
}