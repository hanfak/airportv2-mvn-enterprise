package com.hanfak.airport.infrastructure.dataproviders.database.jdbc;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static java.lang.String.format;
import static java.util.Optional.empty;

// TODO Split for each operation
// TODO interface
// TooManyStaticImports - Acceptable for this class, only using domain objects (static factories and enums)
// PrematureDeclaration - Acceptable for this class, due to iterator pattern of ResultSet
@SuppressWarnings({"PMD.TooManyStaticImports", "PMD.PrematureDeclaration"})
public class AirportStorageJdbcRepository {

  private static final String PLANE_BY_PLANE_ID = "SELECT PLANE_STATUS, PLANE_ID FROM airport WHERE PLANE_ID=?";
  private static final String INSERT_A_PLANE = "INSERT INTO airport (PLANE_ID, PLANE_STATUS) VALUES (?,?)";
  private static final String DELETE_PLANE_FROM_AIRPORT = "DELETE FROM airport WHERE PLANE_ID=?";

  private final Logger logger;
  private final JDBCDatabaseConnectionManager databaseConnectionManager;

  public AirportStorageJdbcRepository(Logger logger, JDBCDatabaseConnectionManager databaseConnectionManager) {
    this.logger = logger;
    this.databaseConnectionManager = databaseConnectionManager;
  }

  public Optional<Plane> read(PlaneId lookupId) {
    logger.info(format("Reading Airport for '%s'", lookupId));
    logger.debug(format("Using sql:%n%s", PLANE_BY_PLANE_ID));

    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement queryStatement = connection.prepareStatement(PLANE_BY_PLANE_ID)) {

      queryStatement.setString(1, lookupId.value);

      try (ResultSet resultSet = queryStatement.executeQuery()) {
        if (!resultSet.next()) {
          logger.info(format("Could not find 'plane' for '%s'", lookupId));
          return empty();
        }

        Optional<String> planeStatusDb = Optional.ofNullable(resultSet.getString("PLANE_STATUS"));
        PlaneId planeId = planeId(resultSet.getString("PLANE_ID"));
        PlaneStatus planeStatus = planeStatusDb
                .map(status -> "FLYING".equals(status) ? FLYING : LANDED)
                .orElse(null);

        if (resultSet.next()) {
          throw new IllegalStateException(format("Found more than one 'plane' for '%s'", lookupId));
        }

        Plane planeInAirport = plane(planeId, planeStatus);
        logger.info(format("Found '%s' for '%s'", planeInAirport, lookupId));
        return Optional.of(planeInAirport);
      }
    } catch (SQLException e) {
      throw new IllegalStateException(format("Failed to read 'plane' for '%s'", lookupId), e);
    }
  }

  public void write(final Plane plane) {
    logger.info(format("Persisting '%s'", plane));
    logger.debug(format("Using sql:%n%s", INSERT_A_PLANE));

    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement insertStatement = connection.prepareStatement(INSERT_A_PLANE)) {

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

  public void delete(String lookupId) {
    logger.info(format("Deleting row with plane id '%s'", lookupId));
    logger.debug(format("Using sql:%n%s", DELETE_PLANE_FROM_AIRPORT));

    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement deleteStatement = connection.prepareStatement(DELETE_PLANE_FROM_AIRPORT)) {
      deleteStatement.setString(1, lookupId);
      int rowAffected = deleteStatement.executeUpdate();
      if (rowAffected != 1) {
        throw new IllegalStateException(format("Tried to delete plane with id '%s' but was not there", lookupId));
      }
      connection.commit();
      logger.info(format("Successfully deleted row with plane id '%s'", lookupId));

    } catch (SQLException e) {
      throw new IllegalStateException(format("Failed to delete plane with id '%s' from airport", lookupId), e);
    }
  }
}
