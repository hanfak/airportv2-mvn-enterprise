package com.hanfak.airport.wiring.configuration;

import com.hanfak.airport.usecase.LandPlaneUseCase;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;

public class Application {

  private final Wiring wiring;

  public Application(Wiring wiring) {
    this.wiring = wiring;
  }
// Example of running application, need to remove when better interface available
  public static void main(String[] args) {
    Application application = new Application(new Wiring());
    System.out.println("Airport application started...");
    application.start();
    System.out.println("Airport application stopped...");
  }

  private void start() {
    LandPlaneUseCase landPlaneUseCase = wiring.landPlaneUseCase();
    landPlaneUseCase.instructPlaneToLand(plane(planeId("A0001"), FLYING));
  }
}
