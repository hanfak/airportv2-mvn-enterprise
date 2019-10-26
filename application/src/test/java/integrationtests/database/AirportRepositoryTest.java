package integrationtests.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static org.assertj.core.api.Assertions.assertThat;

// This is redundent with end to end tests testing the database is working
// Here to show how an end to end test might work
@Ignore // Cannot work in build, should use on own
public class AirportRepositoryTest extends YatspecAcceptanceDatabaseTest {

  @Test
  public void writeAPlaneToAirportDatabase() {
    repository.write(plane1);

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    assertThat(allPlanesFromAirport).contains(plane1);
  }

  @Test
  public void readAPlaneFromPlaneIdFromAirportDatabase() {
    repository.write(plane1);

    Optional<Plane> planeReadFromDatabase = repository.read(PlaneId.planeId("A00015"));

    assertThat(planeReadFromDatabase).contains(plane1);
  }

  @Test
  public void removeAPlaneFromAirportDatabase() {
    repository.write(plane1);

    repository.delete("A00015");

    Optional<Plane> planeReadFromDatabase = repository.read(PlaneId.planeId("A00015"));
    assertThat(planeReadFromDatabase).isEmpty();

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    assertThat(allPlanesFromAirport).doesNotContain(plane1);
  }

  private final Plane plane1 = plane(planeId("A00015"), FLYING);
  private final TestAirportStorageRepository repository = new TestAirportStorageRepository(applicationLogger, databaseConnectionManager);
}
