package integrationtests.database;

import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import com.hanfak.airport.wiring.configuration.Wiring;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.slf4j.LoggerFactory.getLogger;

public class YatspecAcceptanceDatabaseTest {

  @Before
  public void setUp() {
    deleteTableContents("airport");
  }

  protected void deleteTableContents(String tableName) {
    executeSQL("TRUNCATE " + tableName);
  }

  private void executeSQL(String sql) {
    try (Connection connection = databaseConnectionManager.getDBConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.execute();
      if (statement.execute()) {
        throw new IllegalArgumentException(sql);
      }
      connection.commit();
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  final Logger applicationLogger = getLogger(APPLICATION.name());
  private final Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
  private final Path secretsProperties = Paths.get("unused");
  private final Settings settings = loadSettings(LoggerFactory.getLogger(Application.class), appProperties, secretsProperties);
  private final Wiring wiring = Wiring.wiring(settings);
  final JDBCDatabaseConnectionManager databaseConnectionManager = wiring.databaseConnectionManager();
}
