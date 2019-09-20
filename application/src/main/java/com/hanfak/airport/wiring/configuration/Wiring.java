package com.hanfak.airport.wiring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanfak.airport.domain.crosscutting.logging.LoggingUncaughtExceptionHandler;
import com.hanfak.airport.domain.monitoring.HealthCheckProbe;
import com.hanfak.airport.infrastructure.crosscutting.ExecutorServiceConcurrently;
import com.hanfak.airport.infrastructure.crosscutting.GuavaSupplierCaching;
import com.hanfak.airport.infrastructure.crosscutting.TrackingExecutorServiceFactory;
import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.AirportPlaneInventoryService;
import com.hanfak.airport.infrastructure.dataproviders.database.JdbcRepository;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import com.hanfak.airport.infrastructure.dataproviders.weather.OpenWeatherMapService;
import com.hanfak.airport.infrastructure.dataproviders.weather.WeatherClient;
import com.hanfak.airport.infrastructure.dataproviders.weather.WeatherClientUnmarshaller;
import com.hanfak.airport.infrastructure.entrypoints.JsonValidator;
import com.hanfak.airport.infrastructure.entrypoints.RequestUnmarshaller;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneResponseMarshaller;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneServlet;
import com.hanfak.airport.infrastructure.entrypoints.landplane.LandAirplaneWebservice;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck.HealthCheckPageServlet;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck.HealthCheckResultJsonBuilder;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck.HealthCheckResultMarshaller;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.healthcheck.HealthCheckWebService;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.metrics.RegisterMetrics;
import com.hanfak.airport.infrastructure.entrypoints.monitoring.ready.ReadyServlet;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffResponseMarshaller;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffServlet;
import com.hanfak.airport.infrastructure.entrypoints.planetakeoff.AirplaneTakeOffWebservice;
import com.hanfak.airport.infrastructure.healthchecks.DatabaseHealthCheck;
import com.hanfak.airport.infrastructure.healthchecks.WeatherApiHealthCheck;
import com.hanfak.airport.infrastructure.httpclient.LogObfuscator;
import com.hanfak.airport.infrastructure.httpclient.LoggingHttpClient;
import com.hanfak.airport.infrastructure.httpclient.TimerFactory;
import com.hanfak.airport.infrastructure.httpclient.UnirestHttpClient;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.infrastructure.webserver.JettyServletBuilder;
import com.hanfak.airport.infrastructure.webserver.JettyWebServer;
import com.hanfak.airport.usecase.HealthChecksUseCase;
import com.hanfak.airport.usecase.LandPlaneUseCase;
import com.hanfak.airport.usecase.TakeOffUseCase;
import com.hanfak.airport.usecase.WeatherService;
import io.prometheus.client.CollectorRegistry;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.logging.LoggingCategory.AUDIT;
import static org.slf4j.LoggerFactory.getLogger;
// Expected for this class
@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveImports", "PMD.CouplingBetweenObjects"})
public class Wiring {

  protected final static Logger APPLICATION_LOGGER = getLogger(APPLICATION.name()); // add to singletons
  private final Singletons singletons;

  public static class Singletons {
    private final JdbcRepository airportStorageRepository;
    private final JDBCDatabaseConnectionManager databaseConnectionManager;
    final TrackingExecutorServiceFactory trackingExecutorServiceFactory;
    private final Settings settings;

    @SuppressWarnings({"PMD.ExcessiveParameterList"})
    public Singletons(JDBCDatabaseConnectionManager databaseConnectionManager, JdbcRepository airportStorageRepository, TrackingExecutorServiceFactory trackingExecutorServiceFactory, Settings settings) {
      this.airportStorageRepository = airportStorageRepository;
      this.databaseConnectionManager = databaseConnectionManager;
      this.trackingExecutorServiceFactory = trackingExecutorServiceFactory;
      this.settings = settings;
    }
  }

  protected Wiring(Singletons singletons) {
    this.singletons = singletons;
  }

  public static Wiring wiring(Settings settings) {
    HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
    JDBCDatabaseConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(APPLICATION_LOGGER, databaseConnectionPooling);
    TrackingExecutorServiceFactory trackingExecutorServiceFactory = new TrackingExecutorServiceFactory(new LoggingUncaughtExceptionHandler(APPLICATION_LOGGER));
    Singletons singletons = new Singletons(
            databaseConnectionManager,
            new AirportStorageJdbcRepository(APPLICATION_LOGGER, databaseConnectionManager),
            trackingExecutorServiceFactory, settings
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
    return new LandAirplaneWebservice(landPlaneUseCase(), new RequestUnmarshaller(), landAirplaneMarshaller(), jsonValidator(), APPLICATION_LOGGER);
  }

  private JsonValidator jsonValidator() {
    return new JsonValidator(new ObjectMapper());
  }

  private LandAirplaneResponseMarshaller landAirplaneMarshaller() {
    return new LandAirplaneResponseMarshaller();
  }

  private LandPlaneUseCase landPlaneUseCase() {
    return new LandPlaneUseCase(airportPlaneInventoryService(), APPLICATION_LOGGER, weatherService());
  }

  private TakeOffUseCase takeOffUseCase() {
    return new TakeOffUseCase(airportPlaneInventoryService(), APPLICATION_LOGGER);
  }

  private WeatherService weatherService() {
    return new OpenWeatherMapService(new WeatherClient(new LoggingHttpClient(getLogger(AUDIT.name()), new UnirestHttpClient(settings()), new TimerFactory(), new LogObfuscator()
    ), settings(), APPLICATION_LOGGER, new WeatherClientUnmarshaller()));
  }

  public AirportPlaneInventoryService airportPlaneInventoryService() {
    return new AirportPlaneInventoryService(singletons.airportStorageRepository);
  }

  private ServletContextHandler servletContextHandler() {
    return new ServletContextHandler();
  }

  public JettyWebServer jettyWebServer(int port) {
    return new JettyWebServer(port, APPLICATION_LOGGER);
  }

  public JettyServletBuilder jettyWebServerBuilder() {
    return new JettyServletBuilder(servletContextHandler(), jettyWebServer(5555), APPLICATION_LOGGER);
  }

  public ReadyServlet readyPageServlet() {
    return new ReadyServlet();
  }

  public AirplaneTakeOffServlet airplaneTakeOffServlet() {
    return new AirplaneTakeOffServlet(new AirplaneTakeOffWebservice(takeOffUseCase(), new RequestUnmarshaller(), new AirplaneTakeOffResponseMarshaller()));
  }

  public CollectorRegistry registerMetrics() {
    return RegisterMetrics.registerMetrics(statisticsHandler(), new CollectorRegistry(true));
  }

  public StatisticsHandler statisticsHandler() {
    return new StatisticsHandler();
  }

  public HealthCheckPageServlet healthCheckPageServlet() {
    return new HealthCheckPageServlet(healthCheckWebService(), APPLICATION_LOGGER);
  }

  private HealthCheckWebService healthCheckWebService() {
    return new HealthCheckWebService(healthCheckResultMarshaller(), healthChecksUseCase());
  }

  private HealthCheckResultMarshaller healthCheckResultMarshaller() {
    return new HealthCheckResultMarshaller(new HealthCheckResultJsonBuilder());
  }

  private HealthChecksUseCase healthChecksUseCase() {
    List<HealthCheckProbe> healthCheckProbes = Arrays.asList(new DatabaseHealthCheck(databaseConnectionManager(), settings(), APPLICATION_LOGGER),
            new WeatherApiHealthCheck(settings(), new LoggingHttpClient(APPLICATION_LOGGER, new UnirestHttpClient(settings()), new TimerFactory(), new LogObfuscator())));
    return new HealthChecksUseCase(
            healthCheckProbes,
            new GuavaSupplierCaching(),
            new ExecutorServiceConcurrently(singletons.trackingExecutorServiceFactory, healthCheckProbes.size()), settings());
  }
}
