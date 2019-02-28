package com.hanfak.airport.dataproviders;

import com.hanfak.airport.domain.Plane;
import org.junit.Test;

import static com.hanfak.airport.domain.PlaneId.planeId;
import static com.hanfak.airport.domain.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.PlaneStatus.LANDED;
import static org.assertj.core.api.Assertions.assertThat;


public class AirportHangerServiceTest {

  @Test
  public void shouldHaveAnEmptyHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();

    assertThat(service.planeInventory()).isEmpty();
  }

  @Test
  public void canAddAPlaneToAHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = new Plane(planeId("A0001"), LANDED);

    assertThat(service.addPlane(plane)).isTrue();
    assertThat(service.planeInventory()).contains(plane);
  }

  @Test
  public void canOnlyAddLandedPlanesToAHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = new Plane(planeId("A0001"), FLYING);
    service.addPlane(plane);

    assertThat(service.addPlane(plane)).isFalse();
    assertThat(service.planeInventory()).doesNotContain(plane);
  }

  @Test
  public void canOnlyHaveOneUniquePlaneInTheHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = new Plane(planeId("A0001"), LANDED);
    Plane plane2 = new Plane(planeId("A0001"), LANDED);

    service.addPlane(plane);

    assertThat(service.addPlane(plane2)).isFalse();
    System.out.println(service.planeInventory());

    assertThat(service.planeInventory().size()).isEqualTo(1);
  }

  @Test
  public void removesPlaneFromHanger() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = new Plane(planeId("A0001"), LANDED);
    service.addPlane(plane);

    boolean actionUnderTest = service.removePlane(plane);

    assertThat(service.planeInventory()).doesNotContain(plane);
    assertThat(actionUnderTest).isTrue();
  }

  @Test
  public void checkPlaneIsInAirport() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService();
    Plane plane = new Plane(planeId("A0001"), LANDED);
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