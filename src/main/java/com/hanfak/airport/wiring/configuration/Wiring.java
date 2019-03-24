package com.hanfak.airport.wiring.configuration;

import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.slf4j.Logger;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static org.slf4j.LoggerFactory.getLogger;

public class Wiring {

  public final Singletons singletons;

  private Wiring(Singletons singletons) {
    this.singletons = singletons;
  }

  public static Wiring wiring(Settings settings) {
    Singletons singletons = new Singletons(settings);
    return new Wiring(singletons);
  }

  public static class Singletons {

//    public final DataSourceProvider dataSourceProvider; // To add
    final Settings settings;
    @SuppressWarnings({"PMD.ExcessiveParameterList"})
    Singletons(Settings settings) {
      this.settings = settings;
    }


  }

  private static Logger applicationLogger = getLogger(APPLICATION.name());

  public LandPlaneUseCase landPlaneUseCase() {
    return new LandPlaneUseCase(airportPlaneInventoryService(), applicationLogger);
  }

  public TakeOffUseCase takeOffUseCase() {
    return new TakeOffUseCase(airportPlaneInventoryService(), applicationLogger);
  }

  private AirportPlaneInventoryService airportPlaneInventoryService() {
    return new AirportPlaneInventoryService();
  }
}
