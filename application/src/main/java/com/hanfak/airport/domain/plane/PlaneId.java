package com.hanfak.airport.domain.plane;

import com.hanfak.airport.domain.helper.SingleValueType;

import static java.lang.String.format;

public class PlaneId extends SingleValueType<String> {

  private PlaneId(String planeId) {
    super(validate(planeId));
  }

  public static PlaneId planeId(String planeId) {
    return new PlaneId(planeId);
  }

  private static String validate(String planeId) {
    boolean planeIdContainsRequiredCharacters = planeId.matches("[a-zA-Z0-9]+");
    if (!planeIdContainsRequiredCharacters) {
      throw new IllegalCharacterException(format("planeId, '%s', is not valid: Illegal characters", planeId));
    }

    boolean planeIdIsRequiredLength = planeId.length() < 11 && planeId.length() > 4;
    if (!planeIdIsRequiredLength) {
      throw new IllegalLengthException(format("planeId, '%s', is not valid: Not required length", planeId));
    }

    return planeId;
  }
}
