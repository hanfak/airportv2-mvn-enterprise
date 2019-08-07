package com.hanfak.airport.infrastructure.properties;

public interface WeatherApiSettings {
  String locationLatitude();
  String locationLongitude();
  String weatherUrl();
  String appId();
}
