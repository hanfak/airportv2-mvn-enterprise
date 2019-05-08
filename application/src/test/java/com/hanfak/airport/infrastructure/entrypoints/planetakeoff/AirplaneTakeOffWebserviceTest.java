package com.hanfak.airport.infrastructure.entrypoints.planetakeoff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus;
import com.hanfak.airport.infrastructure.webserver.RenderedContent;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.junit.Test;

import static com.hanfak.airport.domain.AirportStatus.NOT_IN_AIRPORT;
import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.planetakeoffstatus.FailedPlaneTakeOffStatus.failedPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.PlaneTakeOffStatus.createPlaneTakeOffStatus;
import static com.hanfak.airport.domain.planetakeoffstatus.TakeOffFailureReason.PLANE_IS_NOT_AT_THE_AIRPORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AirplaneTakeOffWebserviceTest {
  // Not testing successful response, as this is covered in end to end test
  @Test
  public void createFailedResponse() throws JsonProcessingException {
    when(unmarshaller.unmarshal(request2)).thenReturn(plane(planeId("A0001"), FLYING));
    when(usecase.instructPlaneToTakeOff(plane(planeId("A0001"), FLYING))).thenReturn(statusOfPlane);
    when(marshaller.marshall(failedPlaneTakeOffStatus(planeId("A0001"),
            FLYING, NOT_IN_AIRPORT, PLANE_IS_NOT_AT_THE_AIRPORT))).thenReturn(expectedFailedResponse);

    RenderedContent failedResponse = webservice.execute(request2);

    assertThat(failedResponse).isEqualTo(expectedFailedResponse);
    verify(usecase).instructPlaneToTakeOff(plane(planeId("A0001"), FLYING));
    verify(unmarshaller).unmarshal(request2);
    verify(marshaller).marshall(failedPlaneTakeOffStatus(planeId("A0001"),
            FLYING, NOT_IN_AIRPORT, PLANE_IS_NOT_AT_THE_AIRPORT));
  }

  private final PlaneTakeOffStatus statusOfPlane = createPlaneTakeOffStatus(null,
          failedPlaneTakeOffStatus(planeId("A0001"), FLYING, NOT_IN_AIRPORT, PLANE_IS_NOT_AT_THE_AIRPORT));

  private final String request2 =
          "{\n" +
                  "  \"PlaneId\":\"A0001\",\n" +
                  "  \"PlaneStatus\":\"LANDED\"\n" +
                  "}";
  private String expectedFailedResponseBody =
          "{\n" +
                  "  \"PlaneId\" : \"A0001\",\n" +
                  "  \"PlaneStatus\" : \"FLYING\",\n" +
                  "  \"AirportStatus\" : \"NOT_IN_AIRPORT\",\n" +
                  "  \"LandFailureReason\" : \"Plane could not take off as it is not in the airport\"\n" +
                  "}";
  private final RenderedContent expectedFailedResponse = new RenderedContent(expectedFailedResponseBody, "application/json", 404);

  private TakeOffUseCase usecase = mock(TakeOffUseCase.class);
  private AirplaneTakeOffRequestUnmarshaller unmarshaller = mock(AirplaneTakeOffRequestUnmarshaller.class);
  private AirplaneTakeOffResponseMarshaller marshaller = mock(AirplaneTakeOffResponseMarshaller.class);
  private final AirplaneTakeOffWebservice webservice = new AirplaneTakeOffWebservice(usecase, unmarshaller, marshaller);
}