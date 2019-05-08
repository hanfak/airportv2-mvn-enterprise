package learning.nondbscheduler;

import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.properties.Settings;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class DatabaseProbe implements Job {

  private final Logger logger;
  private final Settings settings;
  private final static Path secretsProperties = Paths.get("unused");
  private final static Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
  private final static HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(loadSettings(getLogger(APPLICATION.name()), appProperties, secretsProperties));

  private final static PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(getLogger(APPLICATION.name()), databaseConnectionPooling);

  public DatabaseProbe() {
    this.logger = getLogger(APPLICATION.name());
    Path secretsProperties = Paths.get("unused");
    Path appProperties = Paths.get("application/target/classes/localhost.application.properties");
    this.settings = loadSettings(logger, appProperties, secretsProperties);
  }

  public static final String ANSI_GREEN = "\u001B[32m";

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    ProbeResult probeResult = excuteProbe();
    System.out.println(ANSI_GREEN + "This is the job for Database probe: " + probeResult + ANSI_GREEN);
  }

  private  ProbeResult excuteProbe() {
    String query = "SELECT count(*);";
    logger.info(format("Check database connection"));
    logger.debug(format("Using SQL:%n%s", query));
    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      if (resultSet.next() && resultSet.getInt(1) == 1) {
        return ProbeResult.success(name(), format("Database test query '%s' was successful", query));
      } else {
        return ProbeResult.failure(name(), format("Database test query '%s' was not successful. Result was not 1.", query));
      }
    } catch (SQLException e) {
      String message = format("SQLException: %s", e.getMessage());
      logger.error(message, e);
      return ProbeResult.failure(name(), message);
    }
  }

  public String name() {
    return String.format("Database Connection to '%s'", settings.databaseUrl());
  }
}

