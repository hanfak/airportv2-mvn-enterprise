package com.hanfak.airport.infrastructure.dataproviders.database.connection;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
// Use settings

public class HikariDatabaseConnectionPooling {
    private static DataSource datasource;

    static DataSource getDataSource() {
        if (datasource == null) {
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/" + "airportlocal");
            config.setUsername("postgres");
            config.setPassword("docker");

            config.setMaximumPoolSize(10);
            config.setAutoCommit(false);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("verifyServerCertificate", "false");
            config.addDataSourceProperty("useSSL", "true");
            config.addDataSourceProperty("connectTimeout", "3000");

            datasource = new HikariDataSource(config);
        }
        return datasource;
    }
}
