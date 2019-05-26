package com.hanfak.airport.infrastructure.healthchecks;

import com.hanfak.airport.domain.monitoring.ProbeResult;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.properties.Settings;
import org.junit.Test;
import testinfrastructure.TestLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.hanfak.airport.domain.monitoring.ProbeStatus.FAIL;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseHealthCheckTest {
  // Happy path not tested as done in end to end test
  @Test
  public void nameMentionsDatabaseURL() {
    DatabaseHealthCheck databaseConnectionProbe = new DatabaseHealthCheck(connectionProvider, settings, logger);

    assertThat(databaseConnectionProbe.name()).isEqualTo("Database Connection to 'blah'");
  }

  @Test
  public void failsOnSQLException() throws Exception {
    SQLException sqlException = new SQLException("message");
    when(connectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenThrow(sqlException);

    DatabaseHealthCheck databaseConnectionProbe = new DatabaseHealthCheck(connectionProvider, settings, logger);

    ProbeResult result = databaseConnectionProbe.probe();

    assertThat(result.description).isEqualTo("SQLException: message");
    assertThat(result.status).isEqualTo(FAIL);
    assertThat(logger.errorLogs()).containsExactly("SQLException: message");
    assertThat(logger.errorCauses()).containsExactly(sqlException);
  }

  @Test
  public void failsWhenTestQueryHasNoResult() throws Exception {
    when(connectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    DatabaseHealthCheck databaseConnectionProbe = new DatabaseHealthCheck(connectionProvider, settings, logger);

    ProbeResult result = databaseConnectionProbe.probe();

    assertThat(result.description).isEqualTo(format("Database test query '%s' was not successful. Result was not 1.", HEALTH_CHECK_QUERY));
    assertThat(result.status).isEqualTo(FAIL);
  }

  @Test
  public void failsWhenTestQueryHasBadResult() throws Exception {
    when(connectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt(1)).thenReturn(999);

    DatabaseHealthCheck databaseConnectionProbe = new DatabaseHealthCheck(connectionProvider, settings, logger);

    ProbeResult result = databaseConnectionProbe.probe();

    assertThat(result.description).isEqualTo(format("Database test query '%s' was not successful. Result was not 1.", HEALTH_CHECK_QUERY));
    assertThat(result.status).isEqualTo(FAIL);
  }

  private Settings settings() {
    Settings settings = mock(Settings.class);
    when(settings.databaseUrl()).thenReturn(DATABASE_URL);
    return settings;
  }

  private static final String DATABASE_URL = "blah";
  private static final String HEALTH_CHECK_QUERY = "SELECT count(*);";

  private final TestLogger logger = new TestLogger();
  private final Settings settings = settings();
  private final JDBCDatabaseConnectionManager connectionProvider = mock(JDBCDatabaseConnectionManager.class);
  private final Connection connection = mock(Connection.class);
  private final PreparedStatement preparedStatement = mock(PreparedStatement.class);
  private final ResultSet resultSet = mock(ResultSet.class);
}