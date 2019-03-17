package learning;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.database.JDBCDatabaseConnectionManager;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCAirportRepositoryTest implements WithAssertions {

  @Test
  public void readsMultipleRecordsFromAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(queryPreparedStatement);
    when(queryPreparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(true, true, false);
    when(resultSet.getString("PLANE_STATUS")).thenReturn("FLYING").thenReturn("LANDED");
    when(resultSet.getString("PLANE_ID")).thenReturn("A0001").thenReturn("B003");

    List<Plane> result =
            repository.getAllPlanesFromAirport();

    assertThat(result).contains(plane1);
    assertThat(result).contains(plane2);
    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, queryPreparedStatement, resultSet);
    inOrder.verify(logger).info("Reading all stored planes at the airport");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(queryPreparedStatement).executeQuery();
    inOrder.verify(resultSet).next();
    inOrder.verify(logger).info("Returning 2 records");

    inOrder.verify(resultSet).close();
    inOrder.verify(queryPreparedStatement).close();
    inOrder.verify(connection).close();
  }

  @Test
  public void readsZeroRecordsFromAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(queryPreparedStatement);
    when(queryPreparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(false);

    List<Plane> result =
            repository.getAllPlanesFromAirport();

    assertThat(result).isEmpty();
    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, queryPreparedStatement, resultSet);
    inOrder.verify(logger).info("Reading all stored planes at the airport");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(queryPreparedStatement).executeQuery();
    inOrder.verify(resultSet).next();
    inOrder.verify(logger).info("Could not find any records");

    inOrder.verify(resultSet).close();
    inOrder.verify(queryPreparedStatement).close();
    inOrder.verify(connection).close();
  }

  @Test
  public void throwsExceptionIfSomeErrorWhenReadingFromDatabase() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(queryPreparedStatement);
    when(queryPreparedStatement.executeQuery()).thenThrow(SQLException.class);

    assertThatThrownBy(repository::getAllPlanesFromAirport)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Failed to read")
            .hasCause(new SQLException());
  }

  private final JDBCDatabaseConnectionManager databaseConnectionProvider = mock(JDBCDatabaseConnectionManager.class);
  private final Logger logger = mock(Logger.class);
  private final Connection connection = mock(Connection.class);
  private final PreparedStatement queryPreparedStatement = mock(PreparedStatement.class);
  private final ResultSet resultSet = mock(ResultSet.class);

  private Plane plane1 = plane(planeId("A0001"), FLYING);
  private Plane plane2 = plane(planeId("B003"), LANDED);

  private final JDBCAirportRepository repository = new JDBCAirportRepository(logger, databaseConnectionProvider);
}