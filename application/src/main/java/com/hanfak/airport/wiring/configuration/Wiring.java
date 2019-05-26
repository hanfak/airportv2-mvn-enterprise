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
import com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics.RegisterMetrics;
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
import io.prometheus.client.CollectorRegistry;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({"PMD.TooManyMethods"})
public class Wiring {

  public final static Logger APPLICATION_LOGGER = getLogger(APPLICATION.name()); // add to singletons
  private final Singletons singletons;

  public static class Singletons {
    private final AirportStorageJdbcRepository airportStorageRepository;
//    @SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD") // Later fix this issue, getters or different type
    private final JDBCDatabaseConnectionManager databaseConnectionManager;
    private final Settings settings;

    @SuppressWarnings({"PMD.ExcessiveParameterList"})
    Singletons(JDBCDatabaseConnectionManager databaseConnectionManager, AirportStorageJdbcRepository airportStorageRepository, Settings settings) {
      this.airportStorageRepository = airportStorageRepository;
      this.databaseConnectionManager = databaseConnectionManager;
      this.settings = settings;
    }
  }

  private Wiring(Singletons singletons) {
    this.singletons = singletons;
  }

  public static Wiring wiring(Settings settings) {
    HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
    JDBCDatabaseConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(APPLICATION_LOGGER, databaseConnectionPooling);
    Singletons singletons = new Singletons(
            databaseConnectionManager,
            new AirportStorageJdbcRepository(APPLICATION_LOGGER, databaseConnectionManager),
            settings
    );
    return new Wiring(singletons);
  }

  public JDBCDatabaseConnectionManager databaseConnectionManager() {
    return singletons.databaseConnectionManager;
  }

  public Settings settings() {
    return singletons.settings;
  }
  public LandAirplaneServlet landAirplaneServlet() {
    return new LandAirplaneServlet(landAirplaneWebservice());
  }

  private LandAirplaneWebservice landAirplaneWebservice() {
    return new LandAirplaneWebservice(landPlaneUseCase(), landAirplaneUnmarshaller(), landAirplaneMarshaller(), jsonValidator(), APPLICATION_LOGGER);
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

  private LandPlaneUseCase landPlaneUseCase() {
    return new LandPlaneUseCase(airportPlaneInventoryService(), APPLICATION_LOGGER);
  }

  private TakeOffUseCase takeOffUseCase() {
    return new TakeOffUseCase(airportPlaneInventoryService(), APPLICATION_LOGGER);
  }

  public AirportPlaneInventoryService airportPlaneInventoryService() {
    return new AirportPlaneInventoryService(singletons.airportStorageRepository);
  }

  private ServletContextHandler servletContextHandler() {
    return new ServletContextHandler();
  }

  public JettyWebServer jettyWebServer(int port) {
    return new JettyWebServer(port);
  }

  public JettyServletBuilder jettyWebServerBuilder() {
    return new JettyServletBuilder(servletContextHandler(), jettyWebServer(5555), APPLICATION_LOGGER);
  }

  public ReadyServlet readyPageServlet() {
    return new ReadyServlet();
  }

  public AirplaneTakeOffServlet airplaneTakeOffServlet() {
    return new AirplaneTakeOffServlet(new AirplaneTakeOffWebservice(takeOffUseCase(), new AirplaneTakeOffRequestUnmarshaller(), new AirplaneTakeOffResponseMarshaller()));
  }

  public CollectorRegistry registerMetrics() {
    return RegisterMetrics.registerMetrics(statisticsHandler(), new CollectorRegistry(true));
  }

  public StatisticsHandler statisticsHandler() {
    return new StatisticsHandler();
  }
}
