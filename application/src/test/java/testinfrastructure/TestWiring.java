package testinfrastructure;

import com.hanfak.airport.infrastructure.dataproviders.JDBCDatabaseConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.dataproviders.database.jdbc.AirportStorageJdbcRepository;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.configuration.Wiring;

public class TestWiring extends Wiring {
  private TestWiring(Singletons singletons) {
    super(singletons);
  }

  static TestWiring testWiring(Settings settings) {
    HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
    JDBCDatabaseConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(APPLICATION_LOGGER, databaseConnectionPooling);
    Singletons singletons = new Singletons(
            databaseConnectionManager,
            new AirportStorageJdbcRepository(APPLICATION_LOGGER, databaseConnectionManager),
            settings
    );
    return new TestWiring(singletons);
  }

//  @Override
//  protected WeatherService weatherService() {
//    return new WeatherServiceStub(false);
//  }

}
