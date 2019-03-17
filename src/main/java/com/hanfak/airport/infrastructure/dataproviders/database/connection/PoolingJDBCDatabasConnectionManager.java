package com.hanfak.airport.infrastructure.dataproviders.database.connection;

import com.hanfak.airport.infrastructure.dataproviders.database.JDBCDatabaseConnectionManager;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class PoolingJDBCDatabasConnectionManager implements JDBCDatabaseConnectionManager {
    private final Logger logger;
    private final Supplier<DataSource> dataSourceSupplier;

    // For testing
    public PoolingJDBCDatabasConnectionManager(Logger logger, DataSource dataSource) {
        this.logger = logger;
        this.dataSourceSupplier = () -> dataSource;
    }

    public PoolingJDBCDatabasConnectionManager(Logger logger) {
        this.logger = logger;
        this.dataSourceSupplier = HikariDatabaseConnectionPooling::getDataSource;
    }

    @Override
    public Connection getDBConnection() {
        DataSource dataSource = dataSourceSupplier.get();

        try {
            logger.info("Getting connection...");
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new IllegalStateException("Cannot connect to db", e);
        }
    }
}
