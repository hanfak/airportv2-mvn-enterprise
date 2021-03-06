package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.WeatherService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import testinfrastructure.stubs.TestAirportPlaneInventoryService;
import testinfrastructure.stubs.TestLogger;
import testinfrastructure.stubs.WeatherServiceStub;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_COULD_NOT_LAND;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.WEATHER_IS_STORMY;


@RunWith(SpecRunner.class)
public class LandPlaneTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanLand() {
    givenTheWeatherIsNotStormy();
    andAPlaneIsFlying();
    andAnAirportHasCapacity();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsInTheAirport();
    andThePlaneIsNotFlying();
  }

  @Test
  public void aPlaneCannotLandWhenPlaneIsInTheAirport() {
    givenTheWeatherIsNotStormy();
    andAPlaneIsAtTheAirport();

    whenAPlaneIsInstructedToLand();

    thenThereIsAFailureInstructingThePlaneToLand();
  }

  @Test
  public void aPlaneCannotLandWhenStormy() {
    givenTheWeatherIsStormy();
    andAPlaneIsFlying();
    andAnAirportHasCapacity();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsNotInTheAirport();
    andThereIsAFailureInstructingThePlaneToLand();
  }

  @Test
  public void aPlaneCannotTakeOffIfNotThereIsASystemError() {
    givenATheApplicationCannotTalkToTheDatabase();

    whenAnotherPlaneIsInstructedToTakeOff();

    thenThereIsASystemFailureStatus();
  }

  private void givenATheApplicationCannotTalkToTheDatabase() {
    givenTheWeatherIsNotStormy();
    plane = plane(planeId("A1111"), FLYING);
    interestingGivens.add("plane", plane);
    airport = new LandPlaneUseCase(testHangerService, logger, weatherService);
  }

  private void andThereIsAFailureInstructingThePlaneToLand() {
    assertThat(logger.infoLogs()).contains("Plane, 'A0001', could not land at the airport as it is stormy");
    assertThat(planeLandStatus.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForStormyWeather);
  }

  private void givenTheWeatherIsStormy() {
    weatherService = new WeatherServiceStub(true);
  }

  private void givenTheWeatherIsNotStormy() {
    weatherService = new WeatherServiceStub(false);
  }

  private void andAPlaneIsAtTheAirport() {
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
    airport = new LandPlaneUseCase(testHangerService, logger, weatherService);
    airport.instructPlaneToLand(plane);
  }

  private void andAPlaneIsFlying() {
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }
  private void andAnAirportHasCapacity() {
    airport = new LandPlaneUseCase(testHangerService, logger, weatherService);
  }

  private void whenAPlaneIsInstructedToLand() {
    planeLandStatus = airport.instructPlaneToLand(plane);
  }

  private void whenAnotherPlaneIsInstructedToTakeOff() {
    planeLandStatus = airport.instructPlaneToLand(plane(planeId("A1111"), FLYING));
  }

  private void thenThereIsAFailureInstructingThePlaneToLand() {
    assertThat(logger.infoLogs()).contains("Plane, 'A0001', cannot land, it is at the airport");
    assertThat(planeLandStatus.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForPresentPlane);
  }

  private void thenThereIsASystemFailureStatus() {
    assertThat(logger.errorLogs()).contains("Something went wrong storing the Plane, 'A1111', at the airport");
    assertThat(planeLandStatus.failedPlaneLandStatus).isEqualTo(expectedFailedPlaneLandStatusForSystemError);
  }

  private void thenthePlaneIsInTheAirport() {
    assertThat(planeLandStatus.successfulPlaneLandStatus).isEqualTo(expectedSuccessfulPlaneLandStatus);
    assertThat(testHangerService.checkPlaneIsAtAirport(plane.planeId)).isTrue();
  }

  private void andThePlaneIsNotFlying() {
    assertThat(testHangerService.planeInventory().get(0).planeStatus).isEqualTo(LANDED);
  }

  private void thenthePlaneIsNotInTheAirport() {
    assertThat(testHangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private final SuccessfulPlaneLandStatus expectedSuccessfulPlaneLandStatus = SuccessfulPlaneLandStatus.successfulPlaneLandStatus(planeId("A0001"), LANDED, IN_AIRPORT);

  private final TestLogger logger = new TestLogger();

  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForPresentPlane = new FailedPlaneLandStatus(planeId("A0001"), FLYING, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForStormyWeather = new FailedPlaneLandStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, WEATHER_IS_STORMY);
  private final FailedPlaneLandStatus expectedFailedPlaneLandStatusForSystemError = failedPlaneLandStatus(planeId("A1111"), FLYING, NOT_IN_AIRPORT, PLANE_COULD_NOT_LAND);
  private Plane plane;
  private PlaneLandStatus planeLandStatus;     // better variable name
  private final TestAirportPlaneInventoryService testHangerService = new TestAirportPlaneInventoryService();
  private WeatherService weatherService;
  private LandPlaneUseCase airport;
}
