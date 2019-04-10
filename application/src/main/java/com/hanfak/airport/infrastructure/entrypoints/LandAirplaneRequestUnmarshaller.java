package com.hanfak.airport.infrastructure.entrypoints;

import com.hanfak.airport.domain.plane.Plane;
import com.hanfak.airport.domain.plane.PlaneId;
import com.hanfak.airport.domain.plane.PlaneStatus;
import org.json.JSONObject;

public class LandAirplaneRequestUnmarshaller {
  public Plane unmarshal(String request) {
    PlaneId planeId = PlaneId.planeId(new JSONObject(request).getString("PlaneId"));
    String planeStatusUnmarshalled = new JSONObject(request).getString("PlaneStatus");
    PlaneStatus planeStatus = "flying".equalsIgnoreCase(planeStatusUnmarshalled) ?
      PlaneStatus.FLYING : PlaneStatus.LANDED;

    return Plane.plane(planeId, planeStatus);
  }
}
