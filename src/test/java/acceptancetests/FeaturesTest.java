package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.dataproviders.AirportHangerService;
import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.usecase.Airport;
import com.hanfak.airport.usecase.HangerService;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;


@RunWith(SpecRunner.class)
public class FeaturesTest extends TestState implements WithAssertions {

  @Test
  public void aPlaneCanLand() {
    givenAPlaneIsFlying();
    andAnAirportHasCapacity();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsInTheAirport();
    andThePlaneIsNotFlying();
  }

  private void givenAPlaneIsFlying() {
    plane = new Plane(planeId("A0001"), FLYING);
    interestingGivens.add("plane", plane);
  }

  private void andAnAirportHasCapacity() {
    airport = new Airport(hangerService);
  }

  private void whenAPlaneIsInstructedToLand() {
    actionDone = airport.instructPlaneToLand(plane);
  }

  private void thenthePlaneIsInTheAirport() {
    assertThat(actionDone).isTrue();
  }

  private void andThePlaneIsNotFlying() {
    System.out.println(hangerService.planeInventory());
    assertThat(hangerService.planeInventory().get(0).planeStatus).isEqualTo(LANDED);
  }


  private Airport airport;
  private Plane plane;
  private boolean actionDone;     // better variable name
  private HangerService hangerService = new AirportHangerService(); // Should use a stub
}
