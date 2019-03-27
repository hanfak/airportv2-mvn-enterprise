package learning.webserver.test;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import learning.webserver.example.WebServerRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpecRunner.class)
public abstract class YatspecAcceptanceEndToEndTest implements WithTestState, WithCustomResultListeners {

    public final TestState testState = new TestState();
    private final WebServerRunner application = new WebServerRunner();

    @Before
    public void startup() {
        application.startWebServer();
    }

    @After
    public void teardown() {
        application.stop();
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
}
