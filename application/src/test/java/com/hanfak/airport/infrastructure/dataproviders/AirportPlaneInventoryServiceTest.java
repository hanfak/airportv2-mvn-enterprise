package com.hanfak.airport.infrastructure.dataproviders;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import org.junit.Test;

import java.util.Optional;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Better test names
public class AirportPlaneInventoryServiceTest {
  private final AirportStorageJdbcRepository airportStorageRepository = mock(AirportStorageJdbcRepository.class);

  @Test
  public void canAddAPlaneToAHanger() {
    Plane plane = plane(planeId("A0001"), LANDED);
    when(airportStorageRepository.read(planeId("A0001"))).thenReturn(empty());
    AirportPlaneInventoryService service = new AirportPlaneInventoryService(airportStorageRepository);

    service.addPlane(plane);

    verify(airportStorageRepository).write(plane);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void canOnlyHaveOneUniquePlaneInTheHanger() {
    Plane plane = plane(planeId("A0001"), LANDED);
    when(airportStorageRepository.read(planeId("A0001"))).thenReturn(empty(), Optional.of(plane));

    AirportPlaneInventoryService service = new AirportPlaneInventoryService(airportStorageRepository);
    service.addPlane(plane);

    verify(airportStorageRepository).write(plane);
    assertThatThrownBy(() -> service.addPlane(plane))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Plane, 'A0001', in airport, cannot store plane in airport");
  }

  @Test
  public void removesPlaneFromHanger() {
    Plane plane = plane(planeId("A0001"), LANDED);
    AirportPlaneInventoryService service = new AirportPlaneInventoryService(airportStorageRepository);
    when(airportStorageRepository.read(planeId("A0001"))).thenReturn(Optional.of(plane));

    service.removePlane(plane);

    verify(airportStorageRepository).delete(plane.planeId.value);
  }

  @Test
  public void cannotRemovePlaneFromAirportWhenPlaneNotInTheAirport() {
    AirportPlaneInventoryService service = new AirportPlaneInventoryService(airportStorageRepository);
    Plane plane = plane(planeId("A0001"), LANDED);
    when(airportStorageRepository.read(planeId("A0001"))).thenReturn(empty());


    assertThatThrownBy(() -> service.removePlane(plane))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Plane, 'A0001', not in airport, cannot remove plane");
  }
}