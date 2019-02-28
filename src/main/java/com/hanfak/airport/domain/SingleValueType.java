package com.hanfak.airport.domain;
//Test
public class SingleValueType<T> extends ValueType {

  private final T value;

  SingleValueType(T value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
