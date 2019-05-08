package integrationtests.rest.readypage;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.hanfak.airport.infrastructure.properties.Settings;
import com.hanfak.airport.wiring.Application;
import com.hanfak.airport.wiring.configuration.Wiring;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.airport.infrastructure.logging.LoggingCategory.APPLICATION;
import static com.hanfak.airport.infrastructure.properties.SettingsLoader.loadSettings;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("SameParameterValue") // readability
@RunWith(SpecRunner.class)
public class ReadyPageTwoTest extends TestState implements WithCustomResultListeners {

  @Test
  public void readyPageRespondsWith200() throws Exception {
    when(weMakeAGetRequestTo(apiUrl));

    thenTheResponseBodyIs("OK");
    andItReturnsAStatusCodeOf(200);
  }

  private ActionUnderTest weMakeAGetRequestTo(String uri) {
    return (interestingGivens, capturedInputAndOutputs) -> whenWeMakeARequestTo(capturedInputAndOutputs, new HttpGet(uri));
  }

  private CapturedInputAndOutputs whenWeMakeARequestTo(CapturedInputAndOutputs capturedInputAndOutputs, HttpGet request) throws IOException {
    capturedInputAndOutputs.add(format("Request from %s to %s", "a_user", "application"), request);
    response = HttpClientBuilder.create().build().execute(request);
    responseBody = EntityUtils.toString(response.getEntity());
    capturedInputAndOutputs.add(format("Response from %s to %s", "application", "a_user"), response.getStatusLine().toString());
    return capturedInputAndOutputs;
  }

  private void thenTheResponseBodyIs(String expected) {
    assertThat(responseBody).isEqualTo(expected);
  }

  private void andItReturnsAStatusCodeOf(int expected) {
    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(expected);
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

  @Before
  public void setUp() {
    application.startWebserver();
  }

  @After
  public void tearDown() {
    application.stopWebServer();
    capturedInputAndOutputs.add("Sequence Diagram", generateSequenceDiagram());
  }

  private Logger applicationLogger = getLogger(APPLICATION.name());
  private Path appProperties = Paths.get("target/test-classes/localhost.test.properties");
  private Path secretsProperties = Paths.get("unused");
  private Settings settings = loadSettings(applicationLogger, appProperties, secretsProperties);

  private final Wiring wiring = Wiring.wiring(settings);

  private final Application application = new Application(wiring);
  private HttpResponse response;

  private String responseBody;
  private final String apiPath = "/ready";
  @SuppressWarnings("FieldCanBeLocal") // readability
  private final String apiUrl = "http://localhost:5555" + apiPath;
}
