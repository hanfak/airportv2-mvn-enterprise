package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TakeOffUseCaseTest implements WithAssertions {

    @Test
    public void removesPlaneFromAirportWhenInstructToTakeOff() {
        PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(plane);

        verify(hangerService).removePlane(plane);
        verify(logger).info(eq("Plane, 'A0001', has successfully left the airport"));
        assertThat(actionUnderTest.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
    }

    @Test
    public void planeDoesNotLeaveAirportWhenPlaneIsFlying() {
        PlaneTakeOffStatus actionUnderTest = takeOffUseCase.instructPlaneToTakeOff(flyingPlane);

        verify(hangerService, never()).removePlane(plane);
        verify(logger).info("Plane, 'A0001', cannot take off, status is 'FLYING'");
        assertThat(actionUnderTest.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForFlyingPlane);
    }

    private final PlaneInventoryService hangerService = mock(PlaneInventoryService.class);
    private final Logger logger = mock(Logger.class);

    private final TakeOffUseCase takeOffUseCase = new TakeOffUseCase(hangerService, logger);
    private final Plane plane = plane(planeId("A0001"), LANDED);
    private final Plane flyingPlane = plane(planeId("A0001"), FLYING);
    private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForFlyingPlane = failedPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, PLANE_IS_FLYING);
    private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
}