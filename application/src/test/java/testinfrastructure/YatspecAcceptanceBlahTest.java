package testinfrastructure;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.infrastructure.dataproviders.AirportPlaneInventoryService;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;

// TODO html output should show call to weatherservice
@RunWith(SpecRunner.class)
public abstract class YatspecAcceptanceBlahTest extends TestState implements WithCustomResultListeners {

    private Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
    private Path secretsProperties = Paths.get("unused");
    private Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
    private final TestWiring wiring = TestWiring.testWiring(settings);
    protected final AirportPlaneInventoryService airportPlaneInventoryService = wiring.airportPlaneInventoryService();
    protected final Application application = new Application(wiring);

    @Before
    public void startup() throws Exception {
        deleteTableContents("airport");
        application.startWebserver();
    }

    @After
    public void teardown() {
        application.stopWebServer();
        capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram());
    }

    protected SvgWrapper generateSequenceDiagram() {
        return new SequenceDiagramGenerator().generateSequenceDiagram(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs));
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() {
        return singletonList(new HtmlResultRenderer()
                .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
                .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
    }

    protected void deleteTableContents(String tableName) {
        executeSQL("TRUNCATE " + tableName);
    }

    private void executeSQL(String sql) {
        try (Connection connection = wiring.databaseConnectionManager().getDBConnection();
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
