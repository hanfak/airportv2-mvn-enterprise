package learning.httpclient.okhttp;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Example01 {
  public static void main(String... args) {
    OkHttpClient client = new OkHttpClient();

    String url = "https://publicobject.com/helloworld.txt";
    Request request = new Request.Builder()
            .url(url)
            .build();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IllegalStateException("Unexpected code " + response);
      }

      Headers responseHeaders = response.headers();
      for (int i = 0; i < responseHeaders.size(); i++) {
        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
      }

      System.out.println(response.body().string()); // throws checked exception, not good for streams

      Headers headers = response.headers();
      Set<Map.Entry<String, List<String>>> entries = headers.toMultimap().entrySet();
      String headersOutput = entries.stream()
              .map(x -> String.format("%s: %s\n",
                      x.getKey(),
                      String.join(", ", x.getValue())))
              .collect(Collectors.joining());
      System.out.println(headersOutput);


    } catch (IOException e) { // throw as IllegalStateException
      e.printStackTrace();
    }
  }
}
