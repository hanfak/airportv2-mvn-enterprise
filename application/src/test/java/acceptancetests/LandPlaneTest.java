package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import testinfrastructure.TestAirportPlaneInventoryService;
import testinfrastructure.TestLogger;
import testinfrastructure.WeatherServiceStub;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.WEATHER_IS_STORMY;


@RunWith(SpecRunner.class)
public class LandPlaneTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanLand() {
    givenAPlaneIsFlying();
    andAnAirportHasCapacity();
    andTheWeatherIsNotStormy();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsInTheAirport();
    andThePlaneIsNotFlying();
  }

  @Test
  public void aPlaneCannotLandWhenStormy() {
    givenAPlaneIsFlying();
    andTheWeatherIsStormy();
    andAnAirportHasCapacity();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsNotInTheAirport();
    andThereIsAFailureInstructingThePlaneToLand();
  }

  private void andThereIsAFailureInstructingThePlaneToLand() {
    assertThat(logger.infoLogs()).contains("Plane, 'A0001', could not land at the airport as it is stormy");
    assertThat(planeLandStatus.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForStormyWeather);

  }

  private void thenthePlaneIsNotInTheAirport() {
    assertThat(testHangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private void andTheWeatherIsStormy() {
    weatherService = new WeatherServiceStub(true);
  }

  // Move to module test so that plane service is active, or move to documentation test and use a stub

  @Test
  public void aPlaneCannotLandWhenPlaneIsInTheAirport() {
    givenAPlaneIsAtTheAirport();
    andTheWeatherIsNotStormy();

    whenAPlaneIsInstructedToLand();

    thenThereIsAFailureInstructingThePlaneToLand();
  }
  private void andTheWeatherIsNotStormy() {
  }

  private void givenAPlaneIsAtTheAirport() {
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
    airport = new LandPlaneUseCase(testHangerService, logger, notStormyWeatherService);
    airport.instructPlaneToLand(plane);
  }

  private void givenAPlaneIsFlying() {
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andAnAirportHasCapacity() {
    airport = new LandPlaneUseCase(testHangerService, logger, weatherService);
  }

  private void whenAPlaneIsInstructedToLand() {
    planeLandStatus = airport.instructPlaneToLand(plane);
  }

  private void thenThereIsAFailureInstructingThePlaneToLand() {
//    verify(logger).error(eq("Plane, 'A0001', is at airport"), any(IllegalStateException.class)); // test in unit test for exception
    assertThat(logger.errorLogs()).contains("Plane, 'A0001', is at airport");
    assertThat(planeLandStatus.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForPresentPlane);
  }

  private void thenthePlaneIsInTheAirport() {
    assertThat(planeLandStatus.successfulPlaneLandStatus).isEqualTo(expectedSuccessfulPlaneLandStatus);
    assertThat(testHangerService.checkPlaneIsAtAirport(plane.planeId)).isTrue();
  }

  private void andThePlaneIsNotFlying() {
    assertThat(testHangerService.planeInventory().get(0).planeStatus).isEqualTo(LANDED);
  }

  private final SuccessfulPlaneLandStatus expectedSuccessfulPlaneLandStatus = SuccessfulPlaneLandStatus.successfulPlaneLandStatus(planeId("A0001"), LANDED, IN_AIRPORT);

  private final TestLogger logger = new TestLogger();

  private FailedPlaneLandStatus expectedFailedPlaneLandStatusForPresentPlane = new FailedPlaneLandStatus(planeId("A0001"), FLYING, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT);
  private FailedPlaneLandStatus expectedFailedPlaneLandStatusForStormyWeather = new FailedPlaneLandStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, WEATHER_IS_STORMY);
  private LandPlaneUseCase airport;
  private Plane plane;
  private PlaneLandStatus planeLandStatus;     // better variable name
  private TestAirportPlaneInventoryService testHangerService = new TestAirportPlaneInventoryService();
  private WeatherServiceStub notStormyWeatherService = new WeatherServiceStub(false);
  private WeatherServiceStub weatherService;
}
