package com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection;

import org.junit.Test;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PoolingJDBCDatabasConnectionManagerTest {

  private final Logger logger = mock(Logger.class);
  private final DataSource dataSource = mock(DataSource.class);
  private final HikariDatabaseConnectionPooling databaseConnectionPooling = mock(HikariDatabaseConnectionPooling.class);
  private final Connection connectionz = mock(Connection.class);

  @Test
  public void getDatabaseConnection() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connectionz);
    when(databaseConnectionPooling.getDataSource()).thenReturn(dataSource);

    PoolingJDBCDatabasConnectionManager poolingJDBCDatabasConnectionManager = new PoolingJDBCDatabasConnectionManager(logger, databaseConnectionPooling);
    Connection dbConnection = poolingJDBCDatabasConnectionManager.getDBConnection();

    assertThat(dbConnection).isEqualTo(connectionz);
    verify(logger).info("Getting connection...");
  }

  @Test
  public void throwExceptionWhenGettingConnectionFails() throws SQLException {
    when(databaseConnectionPooling.getDataSource()).thenReturn(dataSource);
    when(dataSource.getConnection()).thenThrow(SQLException.class);
    PoolingJDBCDatabasConnectionManager poolingJDBCDatabasConnectionManager = new PoolingJDBCDatabasConnectionManager(logger, databaseConnectionPooling);

    assertThatThrownBy(poolingJDBCDatabasConnectionManager::getDBConnection)
            .isInstanceOf(SQLException.class)
            .hasMessage("Cannot connect to db");
  }
}