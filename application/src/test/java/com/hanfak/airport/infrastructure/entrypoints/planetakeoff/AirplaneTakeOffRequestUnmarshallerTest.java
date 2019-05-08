package com.hanfak.airport.infrastructure.entrypoints.planetakeoff;

import com.hanfak.airport.domain.plane.Plane;
import org.junit.Test;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;
import static org.assertj.core.api.Assertions.assertThat;

public class AirplaneTakeOffRequestUnmarshallerTest {

  @Test
  public void unmarshallAirplaneTakeOffWherePlaneIsLandedRequest() {
    Plane unmarshalledRequest = unmarshaller.unmarshal(request1);

    assertThat(unmarshalledRequest).isEqualTo(plane(planeId("A0001"), LANDED));
  }

  @Test
  public void unmarshallAirplaneTakeOffWherePlaneIsFlyingRequest() {
    Plane unmarshalledRequest = unmarshaller.unmarshal(request2);

    assertThat(unmarshalledRequest).isEqualTo(plane(planeId("A0001"), FLYING));
  }

  private final AirplaneTakeOffRequestUnmarshaller unmarshaller = new AirplaneTakeOffRequestUnmarshaller();

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