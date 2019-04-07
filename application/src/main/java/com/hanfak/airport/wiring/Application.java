package com.hanfak.airport.wiring;

import com.hanfak.airport.domain.planelandstatus.PlaneLandStatus;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.wiring.configuration.Wiring;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.domain.plane.Plane.plane;
import static com.hanfak.airport.domain.plane.PlaneId.planeId;
import static com.hanfak.airport.domain.plane.PlaneStatus.FLYING;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static com.hanfak.airport.wiring.configuration.Wiring.wiring;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({"PMD.TooManyStaticImports", "PMD.SystemPrintln"}) // These all refer to domain objects
public class Application {

  private final Wiring wiring;

  public Application(Wiring wiring) {
    this.wiring = wiring;
  }
// Example of running application, need to remove when better interface available
  public static void main(String... args) {
    Path appProperties = Paths.get("target/classes/localhost.application.properties");
    Path secretsProperties = Paths.get("unused");
    Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
    Application application = new Application(wiring(settings));
    System.out.println("Airport application started...");
    application.start();
    System.out.println("Airport application stopped...");
  }

  private void start() {
    LandPlaneUseCase landPlaneUseCase = wiring.landPlaneUseCase();
    PlaneLandStatus a0001 = landPlaneUseCase.instructPlaneToLand(plane(planeId("A0001"), FLYING));
    System.out.println(a0001.failedPlaneLandStatus);
  }
}
