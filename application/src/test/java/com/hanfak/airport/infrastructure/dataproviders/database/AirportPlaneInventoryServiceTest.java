package com.hanfak.airport.infrastructure.dataproviders.database;

import com.hanfak.airport.domain.plane.Plane;
import org.junit.Test;

import java.util.Optional;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Better test names
public class AirportPlaneInventoryServiceTest {

  @Test
  public void canAddAPlaneToAHanger() {
    service.addPlane(plane);

    verify(airportStorageRepository).write(plane);
  }

  @Test
  public void removesPlaneFromHanger() {
    service.addPlane(plane);

    service.removePlane(plane);

    verify(airportStorageRepository).delete(plane.planeId.value);
  }

  @Test
  public void checksThatAPlaneIsInTheAirport() {
    when(airportStorageRepository.read(plane.planeId)).thenReturn(Optional.of(plane));

    boolean airportLocationOfPlane = service.planeIsPresentInAirport(plane);

    verify(airportStorageRepository).read(plane.planeId);
    assertTrue(airportLocationOfPlane);
  }

  @Test
  public void checksThatAPlaneIsNotInTheAirport() {
    when(airportStorageRepository.read(plane.planeId)).thenReturn(Optional.empty());

    boolean airportLocationOfPlane = service.planeIsPresentInAirport(plane);

    verify(airportStorageRepository).read(plane.planeId);
    assertFalse(airportLocationOfPlane);
  }

  private final JdbcRepository airportStorageRepository = mock(JdbcRepository.class);
  private final Plane plane = plane(planeId("A0001"), LANDED);
  private final AirportPlaneInventoryService service = new AirportPlaneInventoryService(airportStorageRepository);
}