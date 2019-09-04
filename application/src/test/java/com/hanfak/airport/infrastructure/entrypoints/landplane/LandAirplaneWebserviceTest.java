package com.hanfak.airport.infrastructure.entrypoints.landplane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.plane.IllegalCharacterException;
import com.hanfak.airport.domain.plane.IllegalLengthException;
import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.infrastructure.entrypoints.JsonValidator;
import com.hanfak.airport.infrastructure.entrypoints.RequestUnmarshaller;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;

import java.util.Optional;

import static com.hanfak.airport.domain.AirportStatus.IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static com.hanfak.airport.domain.planelandstatus.FailedPlaneLandStatus.failedPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.LandFailureReason.PLANE_IS_AT_THE_AIRPORT;
import static com.hanfak.airport.domain.planelandstatus.PlaneLandStatus.createPlaneLandStatus;
import static com.hanfak.airport.domain.planelandstatus.SuccessfulPlaneLandStatus.successfulPlaneLandStatus;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LandAirplaneWebserviceTest {

  @Test
  public void createSuccessfulResponse() throws JsonProcessingException {
    PlaneLandStatus statusOfPlane = createPlaneLandStatus(successfulPlaneLandStatus(planeId("A0001"), FLYING, IN_AIRPORT)
            , null);
    when(usecase.instructPlaneToLand(plane(planeId("A0001"), FLYING))).thenReturn(statusOfPlane);
    when(unmarshaller.unmarshal(request)).thenReturn(plane(planeId("A0001"), FLYING));
    when(marshaller.marshall(successfulPlaneLandStatus(planeId("A0001"),
            FLYING, IN_AIRPORT))).thenReturn(expectedSuccessfulResponse);
    when(jsonValidator.checkForInvalidJson(request)).thenReturn(Optional.empty());

    RenderedContent successfulResponse = webservice.execute(request);

    assertThat(successfulResponse).isEqualTo(expectedSuccessfulResponse);
    verify(usecase).instructPlaneToLand(plane(planeId("A0001"), FLYING));
    verify(unmarshaller).unmarshal(request);
    verify(marshaller).marshall(successfulPlaneLandStatus(planeId("A0001"),
            FLYING, IN_AIRPORT));
  }

  @Test
  public void createFailedResponse() throws JsonProcessingException {
    PlaneLandStatus statusOfPlane = createPlaneLandStatus(null,
            failedPlaneLandStatus(planeId("A0001"),
                    LANDED, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT));
    when(usecase.instructPlaneToLand(plane(planeId("A0001"), LANDED))).thenReturn(statusOfPlane);
    when(unmarshaller.unmarshal(request2)).thenReturn(plane(planeId("A0001"), LANDED));
    when(marshaller.marshall(failedPlaneLandStatus(planeId("A0001"),
            LANDED, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT))).thenReturn(expectedFailedResponse);
    when(jsonValidator.checkForInvalidJson(request)).thenReturn(Optional.empty());

    RenderedContent failedResponse = webservice.execute(request2);

    assertThat(failedResponse).isEqualTo(expectedFailedResponse);
    verify(usecase).instructPlaneToLand(plane(planeId("A0001"), LANDED));
    verify(unmarshaller).unmarshal(request2);
    verify(marshaller).marshall(failedPlaneLandStatus(planeId("A0001"),
            LANDED, IN_AIRPORT, PLANE_IS_AT_THE_AIRPORT));
  }

  @Test
  public void createErrorResponseForInvalidJson() throws JsonProcessingException {
    when(jsonValidator.checkForInvalidJson(request)).thenReturn(Optional.of(new ExceptionMock("Blah")));

    RenderedContent errorResponse = webservice.execute(request);

    verify(logger).error(format("Request body is %s", request));
    verify(logger).error(ArgumentMatchers.eq("Exception"), any(ExceptionMock.class));
    assertThat(errorResponse).isEqualTo(new RenderedContent("Error with JSON Body in request", "text/plain", 500 ));
  }

  @Test
  public void createErrorResponseForWrongLengthValueInPlaneIdfield() throws JsonProcessingException {
    IllegalLengthException illegalLengthException = new IllegalLengthException("blah");
    when(jsonValidator.checkForInvalidJson(request)).thenReturn(Optional.empty());
    when(unmarshaller.unmarshal(request)).thenThrow(illegalLengthException);

    RenderedContent errorResponse = webservice.execute(request);

    verify(logger).error(format("Request body is %s", request), illegalLengthException);
    assertThat(errorResponse)
            .isEqualTo(new RenderedContent("Error with content in body of request: planeId is wrong length",
                    "text/plain",
                    500 ));
  }

  @Test
  public void createErrorResponseForIllegalCharacterInPlaneIdfield() throws JsonProcessingException {
    IllegalCharacterException illegalCharacterException = new IllegalCharacterException("blah");
    when(jsonValidator.checkForInvalidJson(request)).thenReturn(Optional.empty());
    when(unmarshaller.unmarshal(request)).thenThrow(illegalCharacterException);

    RenderedContent errorResponse = webservice.execute(request);

    verify(logger).error(format("Request body is %s", request), illegalCharacterException);
    assertThat(errorResponse)
            .isEqualTo(new RenderedContent("Error with content in body of request: planeId contains illegal character",
                    "text/plain",
                    500 ));
  }

  class ExceptionMock extends JsonProcessingException {
    ExceptionMock(String msg) {
      super(msg);
    }
  }

  private final String request =
          "{\n" +
          "  \"PlaneId\":\"A0001\",\n" +
          "  \"PlaneStatus\":\"FLYING\"\n" +
          "}";
  private final String request2 =
          "{\n" +
          "  \"PlaneId\":\"A0001\",\n" +
          "  \"PlaneStatus\":\"LANDED\"\n" +
          "}";
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
  private final RenderedContent expectedSuccessfulResponse = new RenderedContent(expectedSuccessfulBody, "application/json", 200);
  private final RenderedContent expectedFailedResponse = new RenderedContent(expectedFailedResponseBody, "application/json", 404);

  private final LandPlaneUseCase usecase = mock(LandPlaneUseCase.class);
  private final RequestUnmarshaller unmarshaller = mock(RequestUnmarshaller.class);
  private final LandAirplaneResponseMarshaller marshaller = mock(LandAirplaneResponseMarshaller.class);
  private final Logger logger = mock(Logger.class);
  private final JsonValidator jsonValidator = mock(JsonValidator.class);
  private final LandAirplaneWebservice webservice = new LandAirplaneWebservice(usecase, unmarshaller, marshaller, jsonValidator, logger);
}