package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import testinfrastructure.TestAirportPlaneInventoryService;
import testinfrastructure.TestLogger;
import testinfrastructure.WeatherServiceStub;

import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;

// split to happy and sad path tests
@RunWith(SpecRunner.class)
public class PlaneTakeOffTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneHasLanded();
    andPlaneIsInTheAirport();
    andTheWeatherIsNotStormy();

    whenAPlaneIsInstructedToTakeOff();

    thenThePlaneHasLeftTheAirport();
    andThePlaneIsFlying();
  }

  // Move to module test so that plane service is active, or move to documentation test and use a stub
  @Test
  public void aPlaneCannotTakeOffIfNotAtAirport() {
    givenAPlaneHasLandedSomewhereOutsideTheAirport();
    andTheWeatherIsNotStormy();

    whenAPlaneNotInTheAirportIsInstructedToTakeOff();

    thenThereIsAFailureInstructingThePlaneToTakeOff();
  }

  private void givenAPlaneHasLandedSomewhereOutsideTheAirport() {
    takeOffUseCase = new TakeOffUseCase(testHangerService, logger);
    plane = plane(planeId("A0001"), LANDED);
    interestingGivens.add("plane", plane);
  }

  private void andTheWeatherIsNotStormy() {

  }

  private void givenAPlaneHasLanded() {
    takeOffUseCase = new TakeOffUseCase(testHangerService, logger);
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andPlaneIsInTheAirport() {
    landPlaneUseCase.instructPlaneToLand(plane);
  }

  private void whenAPlaneIsInstructedToTakeOff() {
    whenAPlaneNotInTheAirportIsInstructedToTakeOff();
  }

  private void whenAPlaneNotInTheAirportIsInstructedToTakeOff() {
    planeTakeOffStatus = takeOffUseCase.instructPlaneToTakeOff(plane(planeId("A0001"), LANDED));
  }

  private void thenThereIsAFailureInstructingThePlaneToTakeOff() {
//    verify(logger).info(eq("Plane not at airport"), any(IllegalStateException.class)); // test in unit test
    assertThat(logger.errorLogs()).contains("Plane, 'A0001', is not at airport");
    assertThat(planeTakeOffStatus.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForNotPresentPlane);
  }

  private void thenThePlaneHasLeftTheAirport() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
    assertThat(testHangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private void andThePlaneIsFlying() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus.planeStatus).isEqualTo(FLYING);
  }

  private final TestLogger logger = new TestLogger();
  private final TestAirportPlaneInventoryService testHangerService = new TestAirportPlaneInventoryService();
  private WeatherServiceStub notStormyWeatherService = new WeatherServiceStub(false);

  private final LandPlaneUseCase landPlaneUseCase = new LandPlaneUseCase(testHangerService, logger, notStormyWeatherService);
  private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
  private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForNotPresentPlane = failedPlaneTakeOffStatus(planeId("A0001"), LANDED, NOT_IN_AIRPORT, PLANE_IS_NOT_AT_THE_AIRPORT);
  private TakeOffUseCase takeOffUseCase;
  private PlaneTakeOffStatus planeTakeOffStatus;
  private Plane plane;
}
