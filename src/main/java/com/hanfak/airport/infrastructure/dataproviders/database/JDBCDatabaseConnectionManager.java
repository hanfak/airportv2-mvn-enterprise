package com.hanfak.airport.infrastructure.dataproviders.database;

import java.sql.Connection;

public interface JDBCDatabaseConnectionManager {
    Connection getDBConnection();
}
