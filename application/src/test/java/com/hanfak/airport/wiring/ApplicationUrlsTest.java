package com.hanfak.airport.wiring;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.junit.TableRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static com.hanfak.airport.wiring.ApplicationUrls.LAND_AIRPLANE;
import static com.hanfak.airport.wiring.ApplicationUrls.METRICS_PAGE;
import static com.hanfak.airport.wiring.ApplicationUrls.READY_PAGE;
import static com.hanfak.airport.wiring.ApplicationUrls.TAKE_OFF_AIRPLANE;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TableRunner.class)
public class ApplicationUrlsTest {

  private static final String INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_READY_PAGE = "/re" + "ady";
  private static final String INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_METRICS_PAGE = "/me" + "tri" + "cs";
  private static final String INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_LAND_AIRPLANE = "/lan" + "dAirp" +"lane";
  private static final String INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_TAKE_OFF_AIRPLANE = "/tak" +"eOf" + "fAirp" +"lane";

  @Table({@Row({READY_PAGE, INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_READY_PAGE}),
          @Row({LAND_AIRPLANE, INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_LAND_AIRPLANE}),
          @Row({TAKE_OFF_AIRPLANE, INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_TAKE_OFF_AIRPLANE}),
          @Row({METRICS_PAGE, INTENTIONALLY_SPLIT_UP_TO_AVOID_REFACTORING_METRICS_PAGE})})
  @Test
  public void thirdPartyInternalEndpointsShouldNotChangeWithoutUpdatingTheStubAndThePactContract(String actual, String expected) {
    assertThat(actual).isEqualTo(expected);
  }

  // This test assumes there are no fields other than the url constants
  @Test
  public void blowUpIfAddANewUrlToEnsureItsEndpointIsTestedHere() {
    assertThat(applicationEndpointUrls().size()).isEqualTo(ApplicationUrlsTest.class.getDeclaredFields().length);
  }

  private List<Field> applicationEndpointUrls() {
    return stream(ApplicationUrls.class.getDeclaredFields()).collect(Collectors.toList());
  }
}
