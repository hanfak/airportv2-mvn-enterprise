package com.hanfak.airport.domain.plane;

public class IllegalLengthException extends IllegalArgumentException {
  public IllegalLengthException(String message) {
    super(message);
  }
}
