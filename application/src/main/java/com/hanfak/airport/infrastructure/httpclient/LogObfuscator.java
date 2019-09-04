package com.hanfak.airport.infrastructure.httpclient;

public class LogObfuscator {

  private static final String OBFUSCATE_APP_ID_REGEX = "appid=.*&lon";
  private static final String OBFUSCATED_APP_ID = "appid=******&lon";

  public String obfuscateLogs(String log) {
    return log.replaceAll(OBFUSCATE_APP_ID_REGEX, OBFUSCATED_APP_ID);
  }
}
