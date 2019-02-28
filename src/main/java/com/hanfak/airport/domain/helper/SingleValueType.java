package com.hanfak.airport.domain.helper;
//Test
public class SingleValueType<T> extends ValueType {

  private final T value;

  public SingleValueType(T value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
