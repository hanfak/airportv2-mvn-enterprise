package testinfrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import testinfrastructure.yatspec.ByNamingConventionMessageProducer;
import testinfrastructure.yatspec.RequestAndResponsesFormatter;
import testinfrastructure.yatspec.RequestResponse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;
import static testinfrastructure.TestWiring.testWiring;
import static testinfrastructure.yatspec.YatspecFormatters.toYatspecString;

@RunWith(SpecRunner.class)
public abstract class YatspecAcceptanceEndToEndTest extends TestState implements WithCustomResultListeners {

  public static final int WIREMOCK_PORT = 8888;
  protected static final String APPLICATION_NAME = "Airport Application";
  private static final String THIRD_PARTY_NAME = "Weather API";

  private final Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
  private final Path secretsProperties = Paths.get("unused");
  private final Settings settings = loadSettings(getLogger(Application.class), appProperties, secretsProperties);
  private final TestWiring wiring = testWiring(settings);
  private final RequestAndResponsesFormatter requestAndResponsesFormatter = new RequestAndResponsesFormatter();
  protected final Application application = new Application(wiring);

  private WireMockServer wireMockServer;

  @Before
  public void startup() {
    deleteTableContents("airport");
    startWiremock();
    application.startWebserver();
  }

  @After
  public void teardown() {
    application.stopWebServer();
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram());
    wireMockServer.resetAll();
    wireMockServer.stop();
  }

  protected void registerListener() {
    listenToWiremock(this::recordTraffic);
  }

  private void recordTraffic(Request request, Response response) {
    recordTraffic(request, response, APPLICATION_NAME, THIRD_PARTY_NAME);
  }

  private void startWiremock() {
    wireMockServer = new WireMockServer(WIREMOCK_PORT);
    wireMockServer.start();
  }

  public void recordTraffic(Request request, Response response, String sourceApplication, String destinyApplication) {
    RequestResponse requestResponse = requestResponse(sourceApplication, destinyApplication);
    addToCapturedInputsAndOutputs(requestResponse.request(), toYatspecString(request));
    addToCapturedInputsAndOutputs(requestResponse.response(), toYatspecString(response));
  }

  public RequestResponse requestResponse(String from, String to) {
    return requestAndResponsesFormatter.requestResponse(from, to);
  }

  public void listenToWiremock(RequestListener listener){
    wireMockServer.addMockServiceRequestListener(listener);
  }

  public void addToCapturedInputsAndOutputs(String key, Object capturedStuff){
    testState().capturedInputAndOutputs.add(key, capturedStuff);
  }

  private SvgWrapper generateSequenceDiagram() {
    return new SequenceDiagramGenerator()
            .generateSequenceDiagram(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs));
  }

  @Override
  public Iterable<SpecResultListener> getResultListeners() {
    return singletonList(new HtmlResultRenderer()
            .withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows())
            .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
  }

  @SuppressWarnings("SameParameterValue") // for readability
  private void deleteTableContents(String tableName) {
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
