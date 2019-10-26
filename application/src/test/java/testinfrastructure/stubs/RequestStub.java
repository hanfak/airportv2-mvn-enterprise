package testinfrastructure.stubs;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.Body;
import com.mashape.unirest.request.body.RequestBodyEntity;

import java.util.*;

public class RequestStub extends HttpRequestWithBody {
  private final HttpMethod method;

  public RequestStub(HttpMethod method, String url) {
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
    RequestBodyEntity requestBodyEntity = new RequestBodyEntity(new RequestStub(getHttpMethod(), getUrl()));
    return requestBodyEntity.body("{\n" +
            "  \"PlaneId\" : \"A199234\",\n" +
            "  \"PlaneStatus\" : \"LANDED\",\n" +
            "  \"AirportStatus\" : \"IN_AIRPORT\"\n" +
            "}");
  }

  @Override
  public HttpMethod getHttpMethod() {
    return method;
  }
}
