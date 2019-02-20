package com.hanfak.airport.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ValueTypeTest {
  @Test
  public void shouldConsiderObjectsEqualIfAllTheFieldsAreEqual() {
    Example example1 = new Example("someValue", Arrays.asList("value1", "value2"), 7);
    Example example2 = new Example("someValue", Arrays.asList("value1", "value2"), 7);
    assertThat(example1).isEqualTo(example2);
  }

  @Test
  public void hashCodeOfTwoObjectsWithTheSameFieldsAreEqual() {
    Example example1 = new Example("someValue", Arrays.asList("value1", "value2"), 7);
    Example example2 = new Example("someValue", Arrays.asList("value1", "value2"), 7);
    assertThat(example1.hashCode()).isEqualTo(example2.hashCode());
  }

  @Test
  public void hashCodeOfTwoObjectsWithDifferentFieldsAreDifferent() {
    Example example1 = new Example("someValue1", Arrays.asList("value1", "value2"), 7);
    Example example2 = new Example("someValue2", Arrays.asList("value1", "value2"), 7);
    assertThat(example1.hashCode()).isNotEqualTo(example2.hashCode());
  }

  @Test
  public void toStringContainsFieldNamesAndValues() {
    Example example1 = new Example("someValue1", Arrays.asList("value1", "value2"), 777777);
    assertThat(example1.toString())
            .containsSequence("someField", "someValue1")
            .containsSequence("moreComplicatedField", "value1", "value2")
            .containsSequence("primitiveField", "777777");
  }

  @SuppressWarnings("unused")
  private class Example extends ValueType {
      private final String someField;
      private final List<String> moreComplicatedField;
      private final int primitiveField;

      private Example(String someField, List<String> moreComplicatedField, int primitiveField) {
        this.someField = someField;
        this.moreComplicatedField = moreComplicatedField;
        this.primitiveField = primitiveField;
      }
  }
}