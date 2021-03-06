package com.hanfak.airport.infrastructure.entrypoints.landplane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus;
import com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_COULD_NOT_LAND;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus.successfulPlaneLandStatus;

public class LandAirplaneResponseMarshallerTest {

  @Test
  public void marshallSuccessfulAirplaneLanded() throws JsonProcessingException {
    LandAirplaneResponseMarshaller marshaller = new LandAirplaneResponseMarshaller();

    SuccessfulPlaneLandStatus successfulPlaneLandStatus = successfulPlaneLandStatus(planeId("A0001"),
            LANDED, IN_AIRPORT);
    RenderedContent marshalledResponse = marshaller.marshall(successfulPlaneLandStatus);

    Assertions.assertThat(marshalledResponse).isEqualTo(expectedSuccessfulResponse);
  }

  @Test
  public void marshallFailedAirplaneLanded() throws JsonProcessingException {
    LandAirplaneResponseMarshaller marshaller = new LandAirplaneResponseMarshaller();

    FailedPlaneLandStatus failedPlaneLandStatus = failedPlaneLandStatus(planeId("A0001"),
            LANDED, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT);
    RenderedContent marshalledResponse = marshaller.marshall(failedPlaneLandStatus);

    Assertions.assertThat(marshalledResponse).isEqualTo(expectedFailedResponse);
  }

  @Test
  public void marshallRetriableFailedAirplaneLanded() throws JsonProcessingException {
    LandAirplaneResponseMarshaller marshaller = new LandAirplaneResponseMarshaller();

    FailedPlaneLandStatus failedPlaneLandStatus = failedPlaneLandStatus(planeId("A0001"),
            FLYING, NOT_IN_AIRPORT, PLANE_COULD_NOT_LAND);
    RenderedContent marshalledResponse = marshaller.marshallRetriableFailure(failedPlaneLandStatus);

    Assertions.assertThat(marshalledResponse.body).isEqualTo(expectedRetriableResponseBody);
    Map<String, String> expectedHeaders = new HashMap<>();
    expectedHeaders.put("Retriable", "true");
    Assertions.assertThat(marshalledResponse.headers).isEqualTo(expectedHeaders);
    Assertions.assertThat(marshalledResponse.statusCode).isEqualTo(503);
  }

  private String expectedSuccessfulBody =
          "{\n" +
          "  \"PlaneId\" : \"A0001\",\n" +
          "  \"PlaneStatus\" : \"LANDED\",\n" +
          "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
          "}";
  private String expectedFailedResponseBody =
          "{\n" +
          "  \"PlaneId\" : \"A0001\",\n" +
          "  \"PlaneStatus\" : \"LANDED\",\n" +
          "  \"AirportStatus\" : \"IN_AIRPORT\",\n" +
          "  \"LandFailureReason\" : \"Plane could not land as it is in the airport\"\n" +
          "}";
  @SuppressWarnings("FieldCanBeLocal")
  private String expectedRetriableResponseBody =
          "{\n" +
          "  \"PlaneId\" : \"A0001\",\n" +
          "  \"PlaneStatus\" : \"FLYING\",\n" +
          "  \"AirportStatus\" : \"NOT_IN_AIRPORT\",\n" +
          "  \"LandFailureReason\" : \"Plane could not land at airport, something went wrong with the system\"\n" +
          "}";
  private final RenderedContent expectedSuccessfulResponse = new RenderedContent(expectedSuccessfulBody, "application/json", 200);
  private final RenderedContent expectedFailedResponse = new RenderedContent(expectedFailedResponseBody, "application/json", 404);
}