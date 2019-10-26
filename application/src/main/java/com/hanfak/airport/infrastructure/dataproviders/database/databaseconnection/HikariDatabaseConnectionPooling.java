package com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection;


import com.hanfak.airport.infrastructure.properties.DatabaseSettings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

// Change to non static
public class HikariDatabaseConnectionPooling {

  private final DataSource datasource;
  private final DatabaseSettings settings;

  public HikariDatabaseConnectionPooling(DatabaseSettings settings) {
    this.settings = settings;
    this.datasource = initDataSource();
  }

  public DataSource getDataSource() {
    return datasource;
  }

  private DataSource initDataSource() {
      HikariConfig config = new HikariConfig();

      config.setJdbcUrl(settings.databaseUrl());
      config.setUsername(settings.databaseUser());
      config.setPassword(settings.databasePassword());

      config.setMaximumPoolSize(settings.databaseMaxPoolSize());
      config.setAutoCommit(false);
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      config.addDataSourceProperty("verifyServerCertificate", "false");
      config.addDataSourceProperty("useSSL", "true");
      config.addDataSourceProperty("connectionTimeout", settings.databaseConnectTimeout());

      return new HikariDataSource(config);
  }
}
