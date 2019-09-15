package testinfrastructure.stubs;

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
    if (plane.planeId.equals(PlaneId.planeId("A1111"))) {
      throw new IllegalStateException("Blah");
    }
    planesInventory.add(plane);
  }

  @Override
  public void removePlane(Plane plane) {
    if (plane.planeId.equals(PlaneId.planeId("A11111"))) {
      throw new IllegalStateException("Blah");
    }
    planesInventory.remove(plane);
  }

  @Override
  public boolean planeIsPresentInAirport(Plane plane) {
    return checkPlaneIsAtAirport(plane.planeId);
  }

  public Boolean checkPlaneIsAtAirport(PlaneId planeId) {
    return planesInventory.stream().anyMatch(plane -> planeId.equals(plane.planeId));
  }
}
