package learning;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.connection.PoolingJDBCDatabasConnectionManager;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static org.slf4j.LoggerFactory.getLogger;

public class DatabaseLearningTest implements WithAssertions {
  private final Logger applicationLogger = getLogger(APPLICATION.name());
  private final PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(applicationLogger);

  @Test
  public void getDataFromDatabase() {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }

  @Test
  public void addDataToDatabase() {
    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);
    Plane plane1 = plane(planeId("A00015"), FLYING);

    repository.write(plane1);
    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();

    assertThat(allPlanesFromAirport).contains(plane1);
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }
}
