package com.hanfak.airport.infrastructure.entrypoints;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneStatus;
import org.junit.Test;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestUnmarshallerTest {
  @Test
  public void unmarshallLandAirplaneWherePlaneIsLandedRequest() {
    Plane unmarshalledRequest = unmarshaller.unmarshal(request1);

    assertThat(unmarshalledRequest).isEqualTo(plane(planeId("A0001"), PlaneStatus.LANDED));
  }

  @Test
  public void unmarshallLandAirplaneWherePlaneIsFlyingRequest() {
    Plane unmarshalledRequest = unmarshaller.unmarshal(request2);

    assertThat(unmarshalledRequest).isEqualTo(plane(planeId("A0001"), PlaneStatus.FLYING));
  }

  private final RequestUnmarshaller unmarshaller = new RequestUnmarshaller();

  private final String request1 =
          "{\n" +
                  "  \"PlaneId\":\"A0001\",\n" +
                  "  \"PlaneStatus\":\"Landed\"\n" +
                  "}";
  private final String request2 =
          "{\n" +
                  "  \"PlaneId\":\"A0001\",\n" +
                  "  \"PlaneStatus\":\"Flying\"\n" +
                  "}";
}