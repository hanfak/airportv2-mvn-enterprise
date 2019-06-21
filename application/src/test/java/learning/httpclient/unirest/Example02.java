package learning.httpclient.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.stream.Collectors;

public class Example02 {
  public static void main(String... args) {
    try {
      HttpResponse<String> response = Unirest.get("https://publicobject.com/helloworld.txt")
              .asString();

      requestSuccessful(response.getStatus());

      String headers = response.getHeaders().entrySet().stream()
              .map(x -> String.format("%s: %s\n",
                      x.getKey(),
                      String.join(", ", x.getValue())))
              .collect(Collectors.joining());
      System.out.println("headers = \n" + headers);

      String body = response.getBody();
      System.out.println("body = \n" + body);
    } catch (UnirestException e) {
      e.printStackTrace();
    }

  }

  private static boolean requestSuccessful(int status) {
    return status >= 200 && status < 300;
  }
}
