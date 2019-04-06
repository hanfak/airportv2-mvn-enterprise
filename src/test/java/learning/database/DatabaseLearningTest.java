package learning.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.connection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.connection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.configuration.Application;
import org.assertj.core.api.WithAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.slf4j.LoggerFactory.getLogger;

public class DatabaseLearningTest implements WithAssertions {
  private final Logger applicationLogger = getLogger(APPLICATION.name());
  Path appProperties = Paths.get("target/classes/localhost.application.properties");
  Path secretsProperties = Paths.get("unused");
  Settings settings = loadSettings(LoggerFactory.getLogger(Application.class), appProperties, secretsProperties);
  private final HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
  private final PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(applicationLogger, databaseConnectionPooling);

  @Test
  @Ignore
  public void getDataFromDatabase() {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }

  @Ignore
  @Test
  public void addDataToDatabase() {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);
    Plane plane1 = plane(planeId("A00234"), FLYING);

    repository.write(plane1);
    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();

    assertThat(allPlanesFromAirport).contains(plane1);
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }

  @Ignore
  @Test
  public void addMulitpleDataToDatabase() throws SQLException {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);
    repository.deleteTableContents("airport");

    Plane plane1 = plane(planeId("A00015"), FLYING);
    Plane plane2 = plane(planeId("A00019"), LANDED);
    Plane plane3 = plane(planeId("A00017"), FLYING);
    Plane plane4 = plane(planeId("A00034"), LANDED);
    Plane plane5 = plane(planeId("A00023"), FLYING);
    Plane plane6 = plane(planeId("A00001"), LANDED);
    List<Plane> planes = Arrays.asList(plane1, plane2, plane3, plane4, plane5, plane6);
    repository.writeBatchPlanes(planes);
    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();

    assertThat(allPlanesFromAirport).contains(plane1, plane2);
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }

  @Ignore
  @Test
  public void updateDataToDatabase() {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);
    Plane plane1 = plane(planeId("A00017"), FLYING);

    repository.updateRecord(plane1, 1);
    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();

    assertThat(allPlanesFromAirport).contains(plane1);
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }

  @Ignore
  @Test
  public void deletingAllDataToDatabase() throws SQLException {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);

    repository.deleteTableContents("airport");
    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();

    assertThat(allPlanesFromAirport).isEmpty();
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }

  @Ignore
  @Test
  public void deleteOneRecordByIdToDatabase() throws SQLException {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);

    repository.deleteTableContents("airport");

    Plane plane1 = plane(planeId("A00015"), FLYING);
    Plane plane2 = plane(planeId("A00019"), LANDED);

    repository.write(plane1);
    repository.write(plane2);
    List<Plane> inAirport = repository.getAllPlanesFromAirport();
    System.out.println("inAirport = " + inAirport);

    repository.deleteByRowID(20);
    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();

    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }
}
