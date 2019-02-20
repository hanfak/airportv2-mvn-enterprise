package com.hanfak.airport.domain;


import org.assertj.core.api.WithAssertions;
import org.junit.Test;

public class SingleValueTypeTest implements WithAssertions {

  @Test
  public void returnStringRepresentationOfObject() {
    Example hello = new Example("Hello");

    assertThat(hello.toString()).isEqualTo("Hello");
  }

  @SuppressWarnings("unused")
  private class Example extends SingleValueType<String> {
    private Example(String someField) {
      super(someField);
    }
  }
}