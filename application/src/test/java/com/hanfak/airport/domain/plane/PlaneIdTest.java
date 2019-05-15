package com.hanfak.airport.domain.plane;


import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.junit.TableRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@RunWith(TableRunner.class)
public class PlaneIdTest {

  @Test
  @Table({
          @Row({"12345"}),
          @Row({"1234567"}),
          @Row({"1234567890"})
  })
  public void createsValidPlaneId(String planeId) {
    assertThat(planeId(planeId)).extracting("value").contains(planeId);
  }

  @Test
  @Table({
          @Row({"x", "planeId, 'x', is not valid: Not required length"}),
          @Row({"1234", "planeId, '1234', is not valid: Not required length"}),
          @Row({"1234567890A", "planeId, '1234567890A', is not valid: Not required length"}),
          @Row({"fafasfasfdasdfa", "planeId, 'fafasfasfdasdfa', is not valid: Not required length"}),
  })
  public void throwsIllegalLengthExceptionForInvalidPlaneId(String planeId, String message) {
    assertThatThrownBy(() -> planeId(planeId))
            .isInstanceOf(IllegalLengthException.class)
            .hasMessage(message);
  }

  @Test
  public void throwsIllegalCharacterExceptionForInvalidPlaneId() {
    assertThatExceptionOfType(IllegalCharacterException.class)
            .isThrownBy(() -> planeId("a1\"ced\\e"))
            .withMessage("planeId, 'a1\"ced\\e', is not valid: Illegal characters");
  }

  @Test
  public void throwsIllegalCharacterExceptionForInvalidPlaneIdMultipleCases() {
    String nonAlphanumericCharacters = "!™£§±#$%&'()*+,-./:;<=>?@[]^_`{|}~";
    List<String> illegalCharacterList = Arrays.asList(nonAlphanumericCharacters.split(""));
    illegalCharacterList.forEach(illegalCharacter -> {
      String illegalPlaneId = "abcedef" + illegalCharacter;
      assertThatExceptionOfType(IllegalCharacterException.class)
              .isThrownBy(() -> planeId(illegalPlaneId))
              .withMessage(format("planeId, '%s', is not valid: Illegal characters", illegalPlaneId));
    });
  }
}