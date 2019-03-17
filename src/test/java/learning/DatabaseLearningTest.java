package learning;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.connection.PoolingJDBCDatabasConnectionManager;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static org.slf4j.LoggerFactory.getLogger;

public class DatabaseLearningTest {
  private final Logger applicationLogger = getLogger(APPLICATION.name());
  private final PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(applicationLogger);

  @Test
  public void getDataFromDatabase() {

    JDBCAirportRepository repository = new JDBCAirportRepository(applicationLogger, databaseConnectionManager);

    List<Plane> allPlanesFromAirport = repository.getAllPlanesFromAirport();
    System.out.println("allPlanesFromAirport = " + allPlanesFromAirport);
  }
}
