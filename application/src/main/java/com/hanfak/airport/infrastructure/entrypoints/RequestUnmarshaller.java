package com.hanfak.airport.infrastructure.entrypoints;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import org.json.JSONObject;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.domain.plane.PlaneStatus.LANDED;

public class RequestUnmarshaller {
  public Plane unmarshal(String request) {
    PlaneId planeId = planeId(new JSONObject(request).getString("PlaneId"));
    String planeStatusUnmarshalled = new JSONObject(request).getString("PlaneStatus");
    PlaneStatus planeStatus = "flying".equalsIgnoreCase(planeStatusUnmarshalled) ?
            FLYING : LANDED;

    return plane(planeId, planeStatus);
  }
}
