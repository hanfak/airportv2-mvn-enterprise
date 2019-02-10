package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.hanfak.airport.Airport;
import com.hanfak.airport.Plane;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SpecRunner.class)
public class FeaturesTest implements WithAssertions {

  private Airport airport;
  private Plane plane;

  @Test
  public void aPlaneCanLand() {
    givenAPlaneIsFlying();
    andAnAirportHasCapacity();

    whenAPlaneIsInstructedToLand();

    thenthePlaneIsInTheAirport();
    andThePlaneIsNotFlying();
  }


  private void givenAPlaneIsFlying() {
    plane = new Plane();
  }

  private void andAnAirportHasCapacity() {
    airport = new Airport();
  }

  private void whenAPlaneIsInstructedToLand() {
    airport.instructPlaneToLand(plane);
  }

  private void thenthePlaneIsInTheAirport() {
    assertThat(airport.hanger).contains(plane);
  }

  private void andThePlaneIsNotFlying() {
    assertThat(plane.isFlying()).isFalse();
  }
}
