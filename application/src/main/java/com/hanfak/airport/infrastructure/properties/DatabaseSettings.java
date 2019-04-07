package com.hanfak.airport.infrastructure.properties;

public interface DatabaseSettings {
  String databaseUrl();
  String databaseUser();
  String databasePassword();
  String databaseConnectTimeout();
}
