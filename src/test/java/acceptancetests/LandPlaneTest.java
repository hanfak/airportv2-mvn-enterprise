package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.PlaneInventoryService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(SpecRunner.class)
public class LandPlaneTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanLand() {
    givenAPlaneIsFlying();
    andAnAirportHasCapacity();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsInTheAirport();
    andThePlaneIsNotFlying();
  }

  private void givenAPlaneIsFlying() {
    plane = plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andAnAirportHasCapacity() {
    airport = new LandPlaneUseCase(hangerService, logger);
  }

  private void whenAPlaneIsInstructedToLand() {
    planeLandStatus = airport.instructPlaneToLand(plane);
  }

  private void thenthePlaneIsInTheAirport() {
    verify(logger).info(eq("Plane, 'A0001', has successfully landed at the airport"));

    assertThat(planeLandStatus.successfulPlaneLandStatus).isEqualTo(expectedSuccessfulPlaneLandStatus);
    assertThat(hangerService.checkPlaneIsAtAirport(plane.planeId)).isTrue();
  }

  private void andThePlaneIsNotFlying() {
    assertThat(hangerService.planeInventory().get(0).planeStatus).isEqualTo(LANDED);
  }

  private final SuccessfulPlaneLandStatus expectedSuccessfulPlaneLandStatus = SuccessfulPlaneLandStatus.successfulPlaneLandStatus(planeId("A0001"), LANDED, IN_AIRPORT);

  private final Logger logger = mock(Logger.class);

  private LandPlaneUseCase airport;
  private Plane plane;
  private PlaneLandStatus planeLandStatus;     // better variable name
  private PlaneInventoryService hangerService = new AirportPlaneInventoryService(); // Should use a stub
}
