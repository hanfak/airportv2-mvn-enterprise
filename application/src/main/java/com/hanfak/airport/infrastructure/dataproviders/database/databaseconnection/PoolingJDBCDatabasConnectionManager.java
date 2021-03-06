package com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection;

import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PoolingJDBCDatabasConnectionManager implements JDBCDatabaseConnectionManager {

    private final Logger logger;
    private final HikariDatabaseConnectionPooling databaseConnectionPooling;

    public PoolingJDBCDatabasConnectionManager(Logger logger, HikariDatabaseConnectionPooling databaseConnectionPooling) {
        this.logger = logger;
        this.databaseConnectionPooling = databaseConnectionPooling;
    }

    @Override
    public Connection getDBConnection() throws SQLException {
        DataSource dataSource = databaseConnectionPooling.getDataSource();

        try {
            logger.info("Getting connection...");
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException("Cannot connect to db", e);
        }
    }
}
