package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.PlaneInventoryService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;


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
    airport = new LandPlaneUseCase(hangerService);
  }

  private void whenAPlaneIsInstructedToLand() {
    actionDone = airport.instructPlaneToLand(plane);
  }

  private void thenthePlaneIsInTheAirport() {
    assertThat(actionDone).isTrue();
  }

  private void andThePlaneIsNotFlying() {
    assertThat(hangerService.planeInventory().get(0).planeStatus).isEqualTo(LANDED);
  }

  private LandPlaneUseCase airport;
  private Plane plane;
  private boolean actionDone;     // better variable name
  private PlaneInventoryService hangerService = new AirportPlaneInventoryService(); // Should use a stub
}
