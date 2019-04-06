package com.hanfak.airport.infrastructure.dataproviders.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCDatabaseConnectionManager {
    Connection getDBConnection() throws SQLException;
}
