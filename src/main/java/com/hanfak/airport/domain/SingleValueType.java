package com.hanfak.airport.domain;
//Test
public class SingleValueType<T> extends ValueType {

  public final T value;

  public SingleValueType(T value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
