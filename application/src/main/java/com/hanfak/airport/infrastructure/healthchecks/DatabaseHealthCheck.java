package com.hanfak.airport.infrastructure.healthchecks;

import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.domain.monitoring.ProbeResult;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.properties.DatabaseSettings;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.hanfak.airport.domain.monitoring.ProbeResult.failure;
import static com.hanfak.airport.domain.monitoring.ProbeResult.success;
import static java.lang.String.format;

public class DatabaseHealthCheck implements HealthCheckProbe {

  private final DatabaseSettings settings;
  private final JDBCDatabaseConnectionManager databaseConnectionManager;
  private final Logger logger;

  public DatabaseHealthCheck(JDBCDatabaseConnectionManager databaseConnectionManager, DatabaseSettings settings, Logger logger) {
    this.settings = settings;
    this.databaseConnectionManager = databaseConnectionManager;
    this.logger = logger;
  }

  @Override
  public ProbeResult probe() {
    String query = "SELECT count(*);"; // TODO: put in properties???
    logger.info("Check database connection");
    logger.debug(format("Using SQL:%n%s", query));

    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      if (resultSet.next() && resultSet.getInt(1) == 1) {
        return success(name(), format("Database test query '%s' was successful", query));
      } else {
        return failure(name(), format("Database test query '%s' was not successful. Result was not 1.", query));
      }
    } catch (SQLException e) {
      String message = format("SQLException: %s", e.getMessage());
      logger.error(message, e);
      return failure(name(), message);
    }
  }

  @Override
  public String name() {
    return format("Database Connection to '%s'", settings.databaseUrl());
  }
}
