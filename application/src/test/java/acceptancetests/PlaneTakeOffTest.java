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
import testinfrastructure.stubs.TestAirportPlaneInventoryService;
import testinfrastructure.stubs.TestLogger;
import testinfrastructure.stubs.WeatherServiceStub;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_COULD_NOT_TAKE_OFF;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;

// split to happy and sad path tests
@RunWith(SpecRunner.class)
public class PlaneTakeOffTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanTakeOff() {
    givenAPlaneHasLanded();
    andPlaneIsInTheAirport();

    whenAPlaneIsInstructedToTakeOff();

    thenThePlaneHasLeftTheAirport();
    andThePlaneIsFlying();
  }

  // Move to module test so that plane service is active, or move to documentation test and use a stub
  @Test
  public void aPlaneCannotTakeOffIfNotAtAirport() {
    givenAPlaneHasLandedSomewhereOutsideTheAirport();

    whenAPlaneNotInTheAirportIsInstructedToTakeOff();

    thenThereIsAFailureInstructingThePlaneToTakeOff();
  }

  @Test
  public void aPlaneCannotTakeOffIfNotThereIsASystemError() {
    givenATheApplicationCannotTalkToTheDatabase();

    whenAnotherPlaneIsInstructedToTakeOff();

    thenThereIsASystemFailureStatus();
  }

  private void givenATheApplicationCannotTalkToTheDatabase() {
    givenAnotherPlaneHasLanded();
    andPlaneIsInTheAirport();
  }

  private void givenAPlaneHasLandedSomewhereOutsideTheAirport() {
    takeOffUseCase = new TakeOffUseCase(testHangerService, logger);
    plane = plane(planeId("A0001"), LANDED);
    interestingGivens.add("plane", plane);
  }

  private void givenAPlaneHasLanded() {
    takeOffUseCase = new TakeOffUseCase(testHangerService, logger);
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void givenAnotherPlaneHasLanded() {
    takeOffUseCase = new TakeOffUseCase(testHangerService, logger);
    plane = plane(planeId("A11111"), FLYING);
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

  private void whenAnotherPlaneIsInstructedToTakeOff() {
    planeTakeOffStatus = takeOffUseCase.instructPlaneToTakeOff(plane(planeId("A11111"), LANDED));
  }

  private void thenThereIsAFailureInstructingThePlaneToTakeOff() {
    assertThat(logger.infoLogs()).contains("Plane, 'A0001', cannot take off, it is not at the airport");
    assertThat(planeTakeOffStatus.failedPlaneTakeOffStatus).isEqualTo(expectedFailedPlaneTakeOffStatusForNotPresentPlane);
  }

  private void thenThePlaneHasLeftTheAirport() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus).isEqualTo(expectedSuccessfulPlaneTakeOffStatus);
    assertThat(testHangerService.checkPlaneIsAtAirport(plane.planeId)).isFalse();
  }

  private void thenThereIsASystemFailureStatus() {
    assertThat(logger.errorLogs()).contains("Something went wrong removing the Plane, 'A11111', at the airport");
    assertThat(planeTakeOffStatus.failedPlaneTakeOffStatus).isEqualTo(expectedSystemFailureStatusForNotPresentPlane);
  }

  private void andThePlaneIsFlying() {
    assertThat(planeTakeOffStatus.successfulPlaneTakeOffStatus.planeStatus).isEqualTo(FLYING);
  }

  private final TestLogger logger = new TestLogger();
  private final TestAirportPlaneInventoryService testHangerService = new TestAirportPlaneInventoryService();
  private final WeatherServiceStub notStormyWeatherService = new WeatherServiceStub(false);

  private final LandPlaneUseCase landPlaneUseCase = new LandPlaneUseCase(testHangerService, logger, notStormyWeatherService);
  private final SuccessfulPlaneTakeOffStatus expectedSuccessfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT);
  private final FailedPlaneTakeOffStatus expectedFailedPlaneTakeOffStatusForNotPresentPlane = failedPlaneTakeOffStatus(planeId("A0001"), LANDED, NOT_IN_AIRPORT, PLANE_IS_NOT_AT_THE_AIRPORT);
  private final FailedPlaneTakeOffStatus expectedSystemFailureStatusForNotPresentPlane = failedPlaneTakeOffStatus(planeId("A11111"), LANDED, IN_AIRPORT, PLANE_COULD_NOT_TAKE_OFF);
  private TakeOffUseCase takeOffUseCase;
  private PlaneTakeOffStatus planeTakeOffStatus;
  private Plane plane;
}
