package com.hanfak.airport.wiring.configuration;

import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageRepository;
import com.hanfak.airport.infrastructure.entrypoints.LandAirplaneRequestUnmarshaller;
import com.hanfak.airport.infrastructure.entrypoints.LandAirplaneResponseMarshaller;
import com.hanfak.airport.infrastructure.entrypoints.LandAirplaneServlet;
import com.hanfak.airport.infrastructure.entrypoints.LandAirplaneWebservice;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.infrastructure.webserver.JettyServletBuilder;
import com.hanfak.airport.infrastructure.webserver.JettyWebServer;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.TakeOffUseCase;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({"PMD.TooManyMethods"})
public class Wiring {

  private static Logger applicationLogger = getLogger(APPLICATION.name());
  public final Singletons singletons;

  private Wiring(Singletons singletons) {
    this.singletons = singletons;
  }

  public static Wiring wiring(Settings settings) {
    Singletons singletons = new Singletons(settings);
    return new Wiring(singletons);
  }

  public LandAirplaneServlet landAirplaneServlet() {
    return new LandAirplaneServlet(landAirplaneWebservice());
  }

  private LandAirplaneWebservice landAirplaneWebservice() {
    return new LandAirplaneWebservice(landPlaneUseCase(), landAirplaneUnmarshaller(), landAirplaneMarshaller());
  }

  private LandAirplaneResponseMarshaller landAirplaneMarshaller() {
    return new LandAirplaneResponseMarshaller();
  }

  private LandAirplaneRequestUnmarshaller landAirplaneUnmarshaller() {
    return new LandAirplaneRequestUnmarshaller();
  }

  public static class Singletons {
    //    public final DataSourceProvider dataSourceProvider; // To add
    final Settings settings;
    @SuppressWarnings({"PMD.ExcessiveParameterList"})
    Singletons(Settings settings) {
      this.settings = settings;
    }
  }

  public LandPlaneUseCase landPlaneUseCase() {
    return new LandPlaneUseCase(airportPlaneInventoryService(), applicationLogger);
  }

  public TakeOffUseCase takeOffUseCase() {
    return new TakeOffUseCase(airportPlaneInventoryService(), applicationLogger);
  }

  private AirportPlaneInventoryService airportPlaneInventoryService() {
    return new AirportPlaneInventoryService(airportStorageRepository());
  }
  // TODO should separate to jdbc wiring class
  private AirportStorageRepository airportStorageRepository() {
    return new AirportStorageRepository(applicationLogger, databaseConnectionManager());
  }

  private JDBCDatabaseConnectionManager databaseConnectionManager() {
    return new PoolingJDBCDatabasConnectionManager(applicationLogger, databaseConnectionPooling());
  }

  private HikariDatabaseConnectionPooling databaseConnectionPooling() {
    return new HikariDatabaseConnectionPooling(singletons.settings);
  }

  ServletContextHandler servletContextHandler() {
    return new ServletContextHandler();
  }

  JettyWebServer jettyWebServer(int port) {
    return new JettyWebServer(port);
  }

  public JettyServletBuilder jettyWebServerBuilder() {
    return new JettyServletBuilder(servletContextHandler(), jettyWebServer(5555));
  }
}
