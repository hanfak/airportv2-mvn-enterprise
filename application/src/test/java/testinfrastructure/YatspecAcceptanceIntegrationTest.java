package testinfrastructure;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.HikariDatabaseConnectionPooling;
import com.hanfak.airport.infrastructure.dataproviders.database.databaseconnection.PoolingJDBCDatabasConnectionManager;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import com.hanfak.airport.wiring.configuration.Wiring;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpecRunner.class)
public abstract class YatspecAcceptanceIntegrationTest implements WithTestState, WithCustomResultListeners {

    private Logger applicationLogger = getLogger(APPLICATION.name());
    private Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
    private Path secretsProperties = Paths.get("unused");
    private Settings settings = loadSettings(LoggerFactory.getLogger(Application.class), appProperties, secretsProperties);
    private final TestState testState = new TestState();
    private final Wiring wiring = Wiring.wiring(settings);

    private final HikariDatabaseConnectionPooling databaseConnectionPooling = new HikariDatabaseConnectionPooling(settings);
    private final PoolingJDBCDatabasConnectionManager databaseConnectionManager = new PoolingJDBCDatabasConnectionManager(applicationLogger, databaseConnectionPooling);

    protected final Application application = new Application(wiring);


    @Before
    public void startup() throws Exception {
        deleteTableContents("airport");
        application.startWebserver();
    }

    @After
    public void teardown() {
        application.stopWebServer();
    }

    @Override
    public TestState testState() {
        return testState;
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() {
        List<SpecResultListener> specResultListeners = new ArrayList<>();

        HtmlResultRenderer htmlResultRenderer = new HtmlResultRenderer()
                .withCustomRenderer(String.class, new NewLineAsHtmlBreakRenderer());

        specResultListeners.add(htmlResultRenderer);
        return specResultListeners;
    }

    protected <T> void log(String title, T value) {
        testState.log(title, value);
    }

    public class NewLineAsHtmlBreakRenderer implements Renderer<String> {
        @Override
        public String render(String s) {
            return s.replaceAll("\\n", "<br/>");
        }
    }

    protected void deleteTableContents(String tableName) throws SQLException {
        executeSQL("TRUNCATE " + tableName);
    }

    private void executeSQL(String sql) {
        try (Connection connection = databaseConnectionManager.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
            if (statement.execute()) {
                throw new IllegalArgumentException(sql);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
