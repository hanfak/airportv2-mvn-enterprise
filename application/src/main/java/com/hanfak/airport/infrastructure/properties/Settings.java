package com.hanfak.airport.infrastructure.properties;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;

import java.util.Properties;

public class Settings implements DatabaseSettings{

  private final EnhancedProperties properties;

  @VisibleForTesting
  public Settings(Properties properties, Logger logger) {
    this.properties = new EnhancedProperties(properties, logger);
  }

  @Override
  public String databaseUrl() {
    return properties.getPropertyOrThrowRuntimeException("database.url");
  }

  @Override
  public String databaseUser() {
    return properties.getPropertyOrThrowRuntimeException("database.user");
  }

  @Override
  public String databasePassword() {
    return properties.getPropertyOrThrowRuntimeException("database.password");
  }

  @Override
  public String databaseConnectTimeout() {
    return properties.getPropertyOrThrowRuntimeException("database.connect.timeout");
  }
}