package com.hanfak.airport.domain.plane;

import com.hanfak.airport.domain.helper.SingleValueType;

import static java.lang.String.format;

public class PlaneId extends SingleValueType<String> {

  private PlaneId(String planeId) {
    super(planeId);
    validate();
  }

  public static PlaneId planeId(String planeId) {
    return new PlaneId(planeId);
  }

  private void validate() {
    boolean planeIdContainsRequiredCharacters = value.matches("[a-zA-Z0-9]+");
    if (!planeIdContainsRequiredCharacters) {
      throw new IllegalCharacterException(format("planeId, '%s', is not valid: Illegal characters", value));
    }

    boolean planeIdIsRequiredLength = value.length() < 11 && value.length() > 4;
    if (!planeIdIsRequiredLength) {
      throw new IllegalLengthException(format("planeId, '%s', is not valid: Not required length", value));
    }
  }
}
