package learning;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import com.hanfak.airport.infrastructure.dataproviders.database.JDBCDatabaseConnectionManager;
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

public class JDBCAirportRepository {
  private static final String ALL_PLANES = "SELECT * FROM airport";
  private static final String INSERT_A_PLANE = "INSERT INTO airport (PLANE_ID, PLANE_STATUS) VALUES (?,?)";
  //  private static final String CHECK = "SELECT 1";
  private final Logger logger;
  private final JDBCDatabaseConnectionManager databaseConnectionManager;

  public JDBCAirportRepository(Logger logger, JDBCDatabaseConnectionManager databaseConnectionManager) {
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

  // insert a plane into a airport
  public void write(final Plane plane) {
    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement insertStatement = connection.prepareStatement(INSERT_A_PLANE)) {

      logger.info(format("Persisting '%s'", plane));
      logger.debug(format("Using SQL:%n%s", INSERT_A_PLANE));

      insertStatement.setString(1, plane.planeId.value);
      insertStatement.setString(2, plane.planeStatus.name());
      int rowsAffected = insertStatement.executeUpdate();
      if (rowsAffected != 1) {
        throw new IllegalStateException(format("Something went wrong when trying to persist '%s'. We expected 1 row to be affected but instead %d rows were affected.", plane, rowsAffected));
      }
      connection.commit();
      logger.info(format("Successfully persisted '%s'", plane));
    } catch (SQLException e) {
      throw new IllegalStateException(format("Failed to store plane, '%s', to airport", plane), e);
    }
  }


  // read a plane by id

  // read all planes landed

  // remove a plane

  //   Delete table contents (for clean database, when doing acceptance test in Before block)
//  private void deleteTableContents(String tableName) throws SQLException {
//    executeSQL("DELETE FROM " + tableName);
//  }
//
//  private void executeSQL(String sql) {
//    try (Connection connection = databaseConnectionManager.getDBConnection();
//         PreparedStatement statement = connection.prepareStatement(sql)) {
//      statement.execute();
//      if (statement.execute()) {
//        throw new IllegalArgumentException(sql);
//      }
//      connection.commit();
//    } catch (SQLException e) {
//      throw new IllegalArgumentException(e);
//    }
//  }

}
