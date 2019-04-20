package integrationtests.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import integrationtests.YatspecAcceptanceIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

// This might be redundent with end to end tests
// TODO Use yatspec and BDD language
public class AirportRepositoryTest extends YatspecAcceptanceIntegrationTest {

  @Test
  public void writeAPlaneToAirportDatabase() {
    TestAirportStorageRepository repository = new TestAirportStorageRepository(applicationLogger, databaseConnectionManager);

    repository.write(plane1);

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    assertThat(allPlanesFromAirport).contains(plane1);
    // TODO: use extracting functionality of assertj
  }


  @Test
  public void readAPlaneFromPlaneIdFromAirportDatabase() {
    TestAirportStorageRepository repository = new TestAirportStorageRepository(applicationLogger, databaseConnectionManager);
    repository.write(plane1);

    Optional<Plane> planeReadFromDatabase = repository.read(PlaneId.planeId("A00015"));

    assertThat(planeReadFromDatabase).contains(plane1);
  }

  @Test
  public void removeAPlaneFromAirportDatabase() {
    TestAirportStorageRepository repository = new TestAirportStorageRepository(applicationLogger, databaseConnectionManager);
    repository.write(plane1);

    repository.delete("A00015");

    Optional<Plane> planeReadFromDatabase = repository.read(PlaneId.planeId("A00015"));
    assertThat(planeReadFromDatabase).isEmpty();

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    assertThat(allPlanesFromAirport).doesNotContain(plane1);
  }

  @Before
  public void setUp() throws Exception {
    deleteTableContents("airport");
  }

  private final Logger applicationLogger = getLogger(APPLICATION.name());
  private final Path appProperties = Paths.get("target/classes/localhost.application.properties");
  private final Path secretsProperties = Paths.get("unused");
  private final Settings settings = loadSettings(LoggerFactory.getLogger(Application.class), appProperties, secretsProperties);
  private final HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
  private final PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(applicationLogger, databaseConnectionPooling);
  private final Plane plane1 = plane(planeId("A00015"), FLYING);
}
