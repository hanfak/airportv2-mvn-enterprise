package testinfrastructure;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.usecase.PlaneInventoryService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

// Stub for database
public class TestAirportPlaneInventoryService implements PlaneInventoryService {

  private final List<Plane> planesInventory = new ArrayList<>();

  public List<Plane> planeInventory() {
    return unmodifiableList(planesInventory);
  }

  @Override
  public void addPlane(Plane plane) {
    if (!checkPlaneIsAtAirport(plane.planeId)) {
      planesInventory.add(plane);
    } else {
      // Use custom one
      throw new IllegalStateException(String.format("Plane, '%s', in airport, cannot store plane in airport", plane.planeId));
    }
  }

  @Override
  public void removePlane(Plane plane) { // Should param be plane or planeId?
    if (checkPlaneIsAtAirport(plane.planeId)) {
      planesInventory.remove(plane);
    } else {
      // Use custom one
      throw new IllegalStateException(String.format("Plane, '%s', not in airport, cannot remove plane", plane.planeId));
    }
  }

  public Boolean checkPlaneIsAtAirport(PlaneId planeId) {
    return planesInventory.stream().anyMatch(plane -> planeId.equals(plane.planeId));
  }
}
