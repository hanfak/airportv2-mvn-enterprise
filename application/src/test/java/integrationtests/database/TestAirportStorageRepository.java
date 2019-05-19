package integrationtests.database;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static java.lang.String.format;

public class TestAirportStorageRepository extends AirportStorageJdbcRepository {

  private static final String ALL_PLANES = "SELECT * FROM airport";

  private final Logger logger;
  private final JDBCDatabaseConnectionManager databaseConnectionManager;

  public TestAirportStorageRepository(Logger logger, JDBCDatabaseConnectionManager databaseConnectionManager) {
    super(logger, databaseConnectionManager);
    this.logger = logger;
    this.databaseConnectionManager = databaseConnectionManager;
  }

  public List<Plane> getAllPlanesFromAirport() {
    return executeReadSql();
  }

  private List<Plane> executeReadSql() {
    logger.info(format("Reading all stored planes at the airport"));
    logger.debug(format("Using SQL:%n%s", ALL_PLANES));
    try {
      try (Connection dbConnection = databaseConnectionManager.getDBConnection();
           PreparedStatement statement = dbConnection.prepareStatement(ALL_PLANES)) {
        logger.info("Connected to db");

        try (ResultSet resultSet = statement.executeQuery()) {

          logger.info("starting read");
          List<Plane> planes = new ArrayList<>();
          while (resultSet.next()) {
            String status = resultSet.getString("PLANE_STATUS");
            PlaneStatus planeStatus = status.equals("FLYING") ? FLYING : LANDED;
            PlaneId planeId = planeId(resultSet.getString("PLANE_ID"));
            planes.add(plane(planeId, planeStatus));
          }

          if (planes.isEmpty()) {
            logger.info("Could not find any records");
          } else {
            logger.info(format("Returning %d records", planes.size()));
          }
          return planes;
        }
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to read", e);
    }
  }

}
