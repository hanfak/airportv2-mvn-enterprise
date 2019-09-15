package com.hanfak.airport.infrastructure.dataproviders.database.jdbc;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AirportStorageJdbcRepositoryTest implements WithAssertions {

  @Test
  public void readsPlaneFromAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(queryPreparedStatement);
    when(queryPreparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, false);
    when(resultSet.getString("PLANE_STATUS")).thenReturn("FLYING");
    when(resultSet.getString("PLANE_ID")).thenReturn("A0001");

    Optional<Plane> result =
            repository.read(planeId("A0001"));

    assertThat(result).contains(plane1);

    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, queryPreparedStatement, resultSet);
    inOrder.verify(logger).info("Reading Airport for 'A0001'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(queryPreparedStatement).executeQuery();
    inOrder.verify(resultSet).next();
    inOrder.verify(resultSet).getString("PLANE_STATUS");
    inOrder.verify(resultSet).getString("PLANE_ID");
    inOrder.verify(resultSet).next();
    inOrder.verify(logger).info("Found 'Plane[planeId=A0001,planeStatus=FLYING]' for 'A0001'");
    inOrder.verify(resultSet).close();
    inOrder.verify(queryPreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void findNoPlanesRecordsInAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(queryPreparedStatement);
    when(queryPreparedStatement.executeQuery()).thenReturn(resultSet);

    when(resultSet.next()).thenReturn(false);

    Optional<Plane> result = repository.read(planeId("A0001"));

    assertThat(result.isPresent()).isFalse();
    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, queryPreparedStatement, resultSet);
    inOrder.verify(logger).info("Reading Airport for 'A0001'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(queryPreparedStatement).executeQuery();
    inOrder.verify(resultSet).next();
    inOrder.verify(logger).info("Could not find 'plane' for 'A0001'");

    inOrder.verify(resultSet).close();
    inOrder.verify(queryPreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void findsMoreThanOnePlaneRecordsInAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(queryPreparedStatement);
    when(queryPreparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, true);
    when(resultSet.getString("PLANE_ID")).thenReturn("A0001");

    assertThatThrownBy(() -> repository.read(planeId("A0001")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Found more than one 'plane' for 'A0001'");

    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, queryPreparedStatement, resultSet);
    inOrder.verify(logger).info("Reading Airport for 'A0001'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(queryPreparedStatement).executeQuery();
    inOrder.verify(resultSet).next();
    inOrder.verify(resultSet).getString("PLANE_STATUS");
    inOrder.verify(resultSet).getString("PLANE_ID");
    inOrder.verify(resultSet).next();

    inOrder.verify(resultSet).close();
    inOrder.verify(queryPreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void throwsExceptionWhenSomethingGoesWrongWhenCheckingAPlaneIsInAirportDatabase() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenThrow( new SQLException("blah"));


    assertThatThrownBy(() -> repository.read(planeId("A0001")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Failed to read 'plane' for 'A0001'")
            .hasCause(new SQLException("blah"));
  }

  @Test
  public void addsAPlaneToAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(insertPreparedStatement);
    when(insertPreparedStatement.executeQuery()).thenReturn(resultSet);
    when(insertPreparedStatement.executeUpdate()).thenReturn(1);

    repository.write(plane1);

    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, insertPreparedStatement, resultSet);
    inOrder.verify(logger).info("Persisting 'Plane[planeId=A0001,planeStatus=FLYING]'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(insertPreparedStatement).executeUpdate();
    inOrder.verify(connection).commit();
    inOrder.verify(logger).info("Successfully persisted 'Plane[planeId=A0001,planeStatus=FLYING]'");
    inOrder.verify(insertPreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void cannotAddAPlaneToAirport() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(insertPreparedStatement);
    when(insertPreparedStatement.executeQuery()).thenReturn(resultSet);
    when(insertPreparedStatement.executeUpdate()).thenReturn(2);

    assertThatThrownBy(() -> repository.write(plane1))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Something went wrong when trying to persist 'Plane[planeId=A0001,planeStatus=FLYING]'. We expected 1 row to be affected but instead 2 rows were affected.");

    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, insertPreparedStatement, resultSet);
    inOrder.verify(logger).info("Persisting 'Plane[planeId=A0001,planeStatus=FLYING]'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(insertPreparedStatement).executeUpdate();
    inOrder.verify(insertPreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void throwsExceptionWhenSomethingGoesWrongInPersistingPlaneToAirportDatabase() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(any())).thenThrow(new SQLException("blah"));

    assertThatThrownBy(() -> repository.write(plane1))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Failed to write plane, 'Plane[planeId=A0001,planeStatus=FLYING]', to airport")
            .hasCause(new SQLException("blah"));
  }

  @Test
  public void deleteAPlaneFromAirportDatabase() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(deletePreparedStatement);
    when(deletePreparedStatement.executeUpdate()).thenReturn(1);

    repository.delete(plane1.planeId.value);

    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, deletePreparedStatement, resultSet);
    inOrder.verify(logger).info("Deleting row with plane id 'A0001'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(deletePreparedStatement).executeUpdate();
    inOrder.verify(connection).commit();
    inOrder.verify(logger).info("Successfully deleted row with plane id 'A0001'");
    inOrder.verify(deletePreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void cannotDeleteAPlaneFromAirportDatabaseIfNotThere() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(deletePreparedStatement);
    when(deletePreparedStatement.executeUpdate()).thenReturn(0);

    assertThatThrownBy(() -> repository.delete(plane1.planeId.value))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Tried to delete plane with id 'A0001' but was not there");


    InOrder inOrder = inOrder(logger, databaseConnectionProvider, connection, deletePreparedStatement, resultSet);
    inOrder.verify(logger).info("Deleting row with plane id 'A0001'");
    inOrder.verify(databaseConnectionProvider).getDBConnection();
    inOrder.verify(connection).prepareStatement(any());
    inOrder.verify(deletePreparedStatement).executeUpdate();
    inOrder.verify(deletePreparedStatement).close();
    inOrder.verify(connection).close();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void throwsExceptionWhenSomethingGoesWrongInDeletingPlaneFromAirportDatabase() throws SQLException {
    when(databaseConnectionProvider.getDBConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(deletePreparedStatement);
    when(deletePreparedStatement.executeUpdate()).thenThrow(new SQLException("blah"));

    assertThatThrownBy(() -> repository.delete(plane1.planeId.value))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Failed to delete plane with id 'A0001' from airport")
            .hasCause(new SQLException("blah"));
  }

  private final Plane plane1 = plane(planeId("A0001"), FLYING);
  private final JDBCDatabaseConnectionManager databaseConnectionProvider = mock(JDBCDatabaseConnectionManager.class);
  private final Logger logger = mock(Logger.class);
  private final Connection connection = mock(Connection.class);
  private final PreparedStatement queryPreparedStatement = mock(PreparedStatement.class);
  private final PreparedStatement insertPreparedStatement = mock(PreparedStatement.class);
  private final PreparedStatement deletePreparedStatement = mock(PreparedStatement.class);
  private final ResultSet resultSet = mock(ResultSet.class);
  private final AirportStorageJdbcRepository repository = new AirportStorageJdbcRepository(logger, databaseConnectionProvider);
}