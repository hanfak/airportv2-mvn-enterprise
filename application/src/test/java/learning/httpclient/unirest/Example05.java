package learning.httpclient.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.stream.Collectors;

public class Example05 {
  public static void main(String... args) {
    try {
      String url = "http://api.openweathermap.org/data/2.5/weather?";

      HttpResponse<JsonNode> response = Unirest.get(url)
              .queryString("lat", "51.470020" )
              .queryString("lon", "-0.454296" )
              .queryString("appid", "42f829d2049915097be4c996d1275d8d")
              .asJson();

      requestSuccessful(response.getStatus());

      String headers = response.getHeaders().entrySet().stream()
              .map(x -> String.format("%s: %s\n",
                      x.getKey(),
                      String.join(", ", x.getValue())))
              .collect(Collectors.joining());
      System.out.println("headers = \n" + headers);

      JSONObject body = response.getBody().getObject();
      System.out.println("body = \n" + body);

      JSONArray Jarray = body.getJSONArray("weather");
      JSONObject jsonObject = Jarray.getJSONObject(0);
      // Some sort of enum that maps id to enum in constructor.
      int id = jsonObject.getInt("id");
      System.out.println("id = " + id);
      String main = jsonObject.getString("main");
      System.out.println(main);
    } catch (UnirestException e) {
      e.printStackTrace();
    }

  }

  private static boolean requestSuccessful(int status) {
    return status >= 200 && status < 300;
  }
}
