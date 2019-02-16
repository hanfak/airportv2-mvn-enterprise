package com.hanfak.airport.dataproviders;

import com.hanfak.airport.domain.Plane;
import com.hanfak.airport.domain.PlaneId;
import com.hanfak.airport.domain.PlaneStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AirportHangerServiceTest {

  @Test
  public void shouldHaveAnEmptyHanger() {
    AirportHangerService airportHangerService = new AirportHangerService();

    assertThat(airportHangerService.planeInventory()).isEmpty();
  }

  @Test
  public void canAddAPlaneToAHanger() {
    AirportHangerService airportHangerService = new AirportHangerService();
    Plane plane = new Plane(PlaneId.planeId("A0001"), PlaneStatus.LANDED);

    assertThat(airportHangerService.addPlane(plane)).isTrue();
    assertThat(airportHangerService.planeInventory()).contains(plane);
  }

  @Test
  public void canOnlyAddLandedPlanesToAHanger() {
    AirportHangerService airportHangerService = new AirportHangerService();
    Plane plane = new Plane(PlaneId.planeId("A0001"), PlaneStatus.FLYING);
    airportHangerService.addPlane(plane);

    assertThat(airportHangerService.addPlane(plane)).isFalse();
    assertThat(airportHangerService.planeInventory()).doesNotContain(plane);
  }

  @Test
  public void canOnlyHaveOneUniquePlaneInTheHanger() {
    AirportHangerService airportHangerService = new AirportHangerService();
    Plane plane = new Plane(PlaneId.planeId("A0001"), PlaneStatus.LANDED);
    Plane plane2 = new Plane(PlaneId.planeId("A0001"), PlaneStatus.LANDED);
    airportHangerService.addPlane(plane);

    assertThat(airportHangerService.addPlane(plane2)).isFalse();
    assertThat(airportHangerService.planeInventory().size()).isEqualTo(1);
  }
}