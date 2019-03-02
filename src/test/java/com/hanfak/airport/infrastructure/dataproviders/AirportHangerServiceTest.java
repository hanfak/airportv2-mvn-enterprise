package com.hanfak.airport.infrastructure.dataproviders;

import com.hanfak.airport.domain.plane.Plane;
import org.junit.Test;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AirportHangerServiceTest {

  @Test
  public void shouldHaveAnEmptyHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();

    assertThat(service.planeInventory()).isEmpty();
  }

  @Test
  public void canAddAPlaneToAHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = plane(planeId("A0001"), LANDED);

    service.addPlane(plane);

    assertThat(service.checkPlaneIsAtAirport(plane.planeId)).isTrue();
    assertThat(service.planeInventory()).contains(plane);
  }

  @Test
  public void canOnlyAddLandedPlanesToAHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = plane(planeId("A0001"), FLYING);

    service.addPlane(plane);

    assertThat(service.checkPlaneIsAtAirport(plane.planeId)).isFalse();
    assertThat(service.planeInventory()).doesNotContain(plane);
  }

  @Test
  public void canOnlyHaveOneUniquePlaneInTheHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = plane(planeId("A0001"), LANDED);
    plane(planeId("A0001"), LANDED);
    service.addPlane(plane);

    service.addPlane(plane);

    assertThat(service.checkPlaneIsAtAirport(plane.planeId)).isTrue();
    assertThat(service.planeInventory().size()).isEqualTo(1);
  }

  @Test
  public void removesPlaneFromHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = plane(planeId("A0001"), LANDED);
    service.addPlane(plane);

    service.removePlane(plane);

    assertThat(service.planeInventory()).doesNotContain(plane);
  }

  @Test
  public void cannotRemovePlaneFromHangerWhenPlaneNotAtHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = plane(planeId("A0001"), LANDED);

    assertThatThrownBy(() -> service.removePlane(plane))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Plane, 'A0001', not in airport, cannot remove plane");
  }

  @Test
  public void checkPlaneIsInAirport() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = plane(planeId("A0001"), LANDED);
    service.addPlane(plane);

    boolean planeAtAirport = service.checkPlaneIsAtAirport(planeId("A0001"));

    assertThat(planeAtAirport).isTrue();
  }

  @Test
  public void checkPlaneIsNotAtAirport() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();

    boolean planeAtAirport = service.checkPlaneIsAtAirport(planeId("A0001"));

    assertThat(planeAtAirport).isFalse();
  }
}