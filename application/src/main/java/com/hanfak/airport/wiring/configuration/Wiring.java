package com.hanfak.airport.wiring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import com.hanfak.airport.infrastructure.entrypoints.JsonValidator;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneRequestUnmarshaller;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneResponseMarshaller;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneWebservice;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.ready.ReadyServlet;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffRequestUnmarshaller;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffResponseMarshaller;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffServlet;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffWebservice;
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

  private static Logger applicationLogger = getLogger(APPLICATION.name()); // add to singletons
  public final Singletons singletons;

  public static class Singletons {
    private final AirportStorageJdbcRepository airportStorageRepository;
//    @SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD") // Later fix this issue, getters or different type
    private final JDBCDatabaseConnectionManager databaseConnectionManager;
    @SuppressWarnings({"PMD.ExcessiveParameterList"})
    Singletons(JDBCDatabaseConnectionManager databaseConnectionManager, AirportStorageJdbcRepository airportStorageRepository) {
      this.airportStorageRepository = airportStorageRepository;
      this.databaseConnectionManager = databaseConnectionManager;
    }
  }

  private Wiring(Singletons singletons) {
    this.singletons = singletons;
  }

  public static Wiring wiring(Settings settings) {
    HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
    PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(applicationLogger, databaseConnectionPooling);
    Singletons singletons = new Singletons(
            databaseConnectionManager,
            new AirportStorageJdbcRepository(applicationLogger, databaseConnectionManager)
    );
    return new Wiring(singletons);
  }

  public JDBCDatabaseConnectionManager databaseConnectionManager() {
    return singletons.databaseConnectionManager;
  }

  public LandAirplaneServlet landAirplaneServlet() {
    return new LandAirplaneServlet(landAirplaneWebservice());
  }

  private LandAirplaneWebservice landAirplaneWebservice() {
    return new LandAirplaneWebservice(landPlaneUseCase(), landAirplaneUnmarshaller(), landAirplaneMarshaller(), jsonValidator(), applicationLogger);
  }

  private JsonValidator jsonValidator() {
    return new JsonValidator(new ObjectMapper());
  }

  private LandAirplaneResponseMarshaller landAirplaneMarshaller() {
    return new LandAirplaneResponseMarshaller();
  }

  private LandAirplaneRequestUnmarshaller landAirplaneUnmarshaller() {
    return new LandAirplaneRequestUnmarshaller();
  }

  public LandPlaneUseCase landPlaneUseCase() {
    return new LandPlaneUseCase(airportPlaneInventoryService(), applicationLogger);
  }

  public TakeOffUseCase takeOffUseCase() {
    return new TakeOffUseCase(airportPlaneInventoryService(), applicationLogger);
  }

  public AirportPlaneInventoryService airportPlaneInventoryService() {
    return new AirportPlaneInventoryService(singletons.airportStorageRepository);
  }

  ServletContextHandler servletContextHandler() {
    return new ServletContextHandler();
  }

  public JettyWebServer jettyWebServer(int port) {
    return new JettyWebServer(port);
  }

  public JettyServletBuilder jettyWebServerBuilder() {
    return new JettyServletBuilder(servletContextHandler(), jettyWebServer(5555), applicationLogger);
  }

  public ReadyServlet readyPageServlet() {
    return new ReadyServlet();
  }

  public AirplaneTakeOffServlet airplaneTakeOffServlet() {
    return new AirplaneTakeOffServlet(new AirplaneTakeOffWebservice(takeOffUseCase(), new AirplaneTakeOffRequestUnmarshaller(), new AirplaneTakeOffResponseMarshaller()));
  }
}
