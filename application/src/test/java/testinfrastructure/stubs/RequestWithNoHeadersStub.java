package testinfrastructure.stubs;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.Body;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestWithNoHeadersStub extends HttpRequestWithBody {
  private final HttpMethod method;

  public RequestWithNoHeadersStub(HttpMethod method, String url) {
    super(method, url);
    this.method = method;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public Map<String, List<String>> getHeaders() {
    return new HashMap<>();
  }

  @Override
  public Body getBody() {
    return null;
  }

  @Override
  public HttpMethod getHttpMethod() {
    return method;
  }
}
