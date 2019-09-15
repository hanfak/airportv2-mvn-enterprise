package com.hanfak.airport.usecase;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_COULD_NOT_LAND;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_LANDED;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.WEATHER_IS_STORMY;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.WEATHER_NOT_AVAILABLE;
import static com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus.successfulPlaneLandStatus;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LandPlaneUseCaseTest implements WithAssertions {

  @Test
  public void airportInstructsPlaneToLand() {
    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(planeInventoryService).addPlane(landedPlane);
    verify(logger).info(eq("Plane, 'A0001', has successfully landed at the airport"));
    assertThat(actionUnderTest.successfulPlaneLandStatus).isEqualTo(expectedSuccessfulPlaneLandStatus);
  }

  @Test
  public void cannotInstructPlaneToLandWhenPlaneIsNotFlying() {
    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(landedPlane);

    verify(planeInventoryService, never()).addPlane(landedPlane);
    verify(logger).info("Plane, 'A0001', could not land at the airport as it's status is 'LANDED'");
    assertThat(actionUnderTest.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForLandedPlane);
  }

  @Test
  public void cannotLandWhenWeatherIsStormy() {
    when(weatherService.isStormy()).thenReturn(true);

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(logger).info("Plane, 'A0001', could not land at the airport as it is stormy");
    assertThat(actionUnderTest.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForStormyWeather);
  }

  @Test
  public void cannotLandWhenAccessToWeatherIsNotWorking() {
    IllegalStateException cause = new IllegalStateException("Blah");
    Mockito.doThrow(cause).when(weatherService).isStormy();

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(logger).error("Something went wrong retrieving the weather at the airport", cause);
    assertThat(actionUnderTest.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForWeatherError);
  }

  @Test
  public void cannotLandWhenIssueWithSystem() {
    IllegalStateException cause = new IllegalStateException("Blah");
    Mockito.doThrow(cause).when(planeInventoryService).addPlane(landedPlane);

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(logger).error("Something went wrong storing the Plane, 'A0001', at the airport", cause);
    assertThat(actionUnderTest.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForSystemError);
  }

  @Test
  public void cannotLandWhenSystemErrorForCheckingPlaneInAirport() {
    IllegalStateException cause = new IllegalStateException("Blah");
    Mockito.doThrow(cause).when(planeInventoryService).planeIsPresentInAirport(landedPlane);

    PlaneLandStatus actionUnderTest = airport.instructPlaneToLand(flyingPlane);

    verify(logger).error("Something went wrong storing the Plane, 'A0001', at the airport", cause);
    assertThat(actionUnderTest.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForSystemError);
  }

  @Before
  public void setUp() {
    // Not needed, as the method returns a primitive in WeatherService, which mockito automatically returns a default
    // But for readability, better to show the priming
    when(weatherService.isStormy()).thenReturn(false);
  }

  private final SuccessfulPlaneLandStatus expectedSuccessfulPlaneLandStatus = successfulPlaneLandStatus(planeId("A0001"), LANDED, IN_AIRPORT);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForLandedPlane = failedPlaneLandStatus(planeId("A0001"), LANDED, NOT_IN_AIRPORT, PLANE_IS_LANDED);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForSystemError = failedPlaneLandStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, PLANE_COULD_NOT_LAND);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForWeatherError = failedPlaneLandStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, WEATHER_NOT_AVAILABLE);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForStormyWeather = failedPlaneLandStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, WEATHER_IS_STORMY);

  private final PlaneInventoryService planeInventoryService = mock(PlaneInventoryService.class);
  private final Logger logger = mock(Logger.class);
  private final WeatherService weatherService = mock(WeatherService.class);
  private final LandPlaneUseCase airport = new LandPlaneUseCase(planeInventoryService, logger, weatherService);
  private final Plane flyingPlane = plane(planeId("A0001"), FLYING);
  private final Plane landedPlane = plane(planeId("A0001"), LANDED);
}