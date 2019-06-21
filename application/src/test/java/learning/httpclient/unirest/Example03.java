package learning.httpclient.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.stream.Collectors;

public class Example03 {
  public static void main(String... args) {
    try {
      String url = "http://api.openweathermap.org/data/2.5/weather?";

      HttpResponse<String> response = Unirest.get(url)
              .queryString("q", "London" )
              .queryString("appid", "42f829d2049915097be4c996d1275d8d")
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

      JSONObject Jobject = new JSONObject(body);
      JSONArray Jarray = Jobject.getJSONArray("weather");
      JSONObject jsonObject = Jarray.getJSONObject(0);
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
