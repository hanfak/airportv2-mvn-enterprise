package com.hanfak.airport.infrastructure.dataproviders;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCDatabaseConnectionManager {
    Connection getDBConnection() throws SQLException;
}
