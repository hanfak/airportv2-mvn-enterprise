package testinfrastructure.stubs;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.Body;

import java.util.*;

public class RequestWithNoBodyStub extends HttpRequestWithBody {
  private final HttpMethod method;

  public RequestWithNoBodyStub(HttpMethod method, String url) {
    super(method, url);
    this.method = method;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public Map<String, List<String>> getHeaders() {
    Map<String, List<String>> headers = new HashMap<>();
    headers.put("key1", Arrays.asList("value1", "value2"));
    headers.put("key2", Collections.singletonList("value1"));
    return headers;
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
