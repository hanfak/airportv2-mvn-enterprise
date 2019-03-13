package com.hanfak.airport.wiring.configuration;

import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static org.slf4j.LoggerFactory.getLogger;

public class Wiring {

  protected Logger logger(Class<?> cls) {
    return LoggerFactory.getLogger(cls);
  }

  private Logger applicationLogger = getLogger(APPLICATION.name());

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
