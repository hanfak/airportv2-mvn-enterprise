package com.hanfak.airport.wiring.configuration;

import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wiring {

  protected Logger logger(Class<?> cls) {
    return LoggerFactory.getLogger(cls);
  }

  public LandPlaneUseCase landPlaneUseCase() {
    return new LandPlaneUseCase(airportPlaneInventoryService(), logger(LandPlaneUseCase.class));
  }

  public TakeOffUseCase takeOffUseCase() {
    return new TakeOffUseCase(airportPlaneInventoryService(), logger(TakeOffUseCase.class));
  }

  private AirportPlaneInventoryService airportPlaneInventoryService() {
    return new AirportPlaneInventoryService();
  }
}
