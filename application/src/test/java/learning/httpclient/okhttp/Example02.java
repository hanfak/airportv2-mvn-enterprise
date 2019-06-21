package learning.httpclient.okhttp;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Example02 {
  public static void main(String... args) {
    OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    String url = "http://api.openweathermap.org/data/2.5/weather?";
    HttpUrl parse = Optional.ofNullable(HttpUrl.parse(url))
            .orElseThrow(IllegalStateException::new);
    HttpUrl httpUrl = parse.newBuilder().addQueryParameter("q", "London")
            .addQueryParameter("appid", "42f829d2049915097be4c996d1275d8d")
            .build();
    Request request = new Request.Builder()
            .url(httpUrl)
            .get()
            .build();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IllegalStateException("Unexpected code " + response);
      }

      ResponseBody body = Optional.ofNullable(response.body()).orElseThrow(IllegalStateException::new);
      String string = body.string();
      System.out.println(string); // throws checked exception, not good for streams

      String headers = response.headers().toMultimap().entrySet().stream()
              .map(x -> String.format("%s: %s\n",
                      x.getKey(),
                      String.join(", ", x.getValue())))
              .collect(Collectors.joining());
      System.out.println(headers);

      System.out.println(response.code());

      JSONObject Jobject = new JSONObject(string);
      JSONArray Jarray = Jobject.getJSONArray("weather");
      JSONObject jsonObject = Jarray.getJSONObject(0);
      int id = jsonObject.getInt("id");
      System.out.println("id = " + id);
      String main = jsonObject.getString("main");
      System.out.println(main);


    } catch (IOException e) { // throw as IllegalStateException
      e.printStackTrace();
    }
  }
}
