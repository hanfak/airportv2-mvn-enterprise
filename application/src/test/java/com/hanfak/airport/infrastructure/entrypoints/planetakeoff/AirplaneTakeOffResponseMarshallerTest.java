package com.hanfak.airport.infrastructure.entrypoints.planetakeoff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus;
import com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import org.junit.Test;

import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.SuccessfulPlaneTakeOffStatus.successfulPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_FLYING;
import static org.assertj.core.api.Assertions.assertThat;

public class AirplaneTakeOffResponseMarshallerTest {

  @Test
  public void marshallSuccessfulAirplaneTakeOff() throws JsonProcessingException {
    SuccessfulPlaneTakeOffStatus successfulPlaneTakeOffStatus = successfulPlaneTakeOffStatus(planeId("A0001"),
            FLYING, NOT_IN_AIRPORT);

    RenderedContent marshalledResponse = marshaller.marshall(successfulPlaneTakeOffStatus);

    assertThat(marshalledResponse).isEqualTo(expectedSuccessfulResponse);
  }

  @Test
  public void marshallFailedAirplaneLanded() throws JsonProcessingException {
    FailedPlaneTakeOffStatus failedPlaneTakeOffStatus = failedPlaneTakeOffStatus(planeId("A0001"),
            FLYING, NOT_IN_AIRPORT, PLANE_IS_FLYING);

    RenderedContent marshalledResponse = marshaller.marshall(failedPlaneTakeOffStatus);

    assertThat(marshalledResponse).isEqualTo(expectedFailedResponse);
  }

  private String expectedSuccessfulBody =
          "{\n" +
          "  \"PlaneId\" : \"A0001\",\n" +
          "  \"PlaneStatus\" : \"FLYING\",\n" +
          "  \"AirportStatus\" : \"NOT_IN_AIRPORT\"\n" +
          "}";
  private String expectedFailedResponseBody =
          "{\n" +
          "  \"PlaneId\" : \"A0001\",\n" +
          "  \"PlaneStatus\" : \"FLYING\",\n" +
          "  \"AirportStatus\" : \"NOT_IN_AIRPORT\",\n" +
          "  \"TakeOffFailureReason\" : \"Plane could not take off as it is still Flying\"\n" +
          "}";

  private final AirplaneTakeOffResponseMarshaller marshaller = new AirplaneTakeOffResponseMarshaller();
  private final RenderedContent expectedSuccessfulResponse = new RenderedContent(expectedSuccessfulBody, "application/json", 200);
  private final RenderedContent expectedFailedResponse = new RenderedContent(expectedFailedResponseBody, "application/json", 404);
}