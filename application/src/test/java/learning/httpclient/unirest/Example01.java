package learning.httpclient.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Example01 {
  public static void main(String... args) {
    try {
      HttpResponse<JsonNode> jsonResponse = Unirest.post("http://httpbin.org/post")
              .header("accept", "application/json")
              .queryString("apiKey", "123")
              .field("parameter", "value")
              .field("foo", "bar")
              .asJson();
      System.out.println(jsonResponse);
    } catch (UnirestException e) {
      e.printStackTrace();
    }
  }
}
