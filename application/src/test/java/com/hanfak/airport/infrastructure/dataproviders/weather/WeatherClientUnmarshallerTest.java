package com.hanfak.airport.infrastructure.dataproviders.weather;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeatherClientUnmarshallerTest {
  private final HttpResponse response = mock(HttpResponse.class);
  private final JsonNode body = mock(JsonNode.class);
  private final JSONObject jsonObject = mock(JSONObject.class);
  private final JSONObject jsonObject2 = mock(JSONObject.class);
  private final JSONArray  jsonArray = mock(JSONArray.class);

  @Test
  public void returnWeatherCode() {
    when(response.getBody()).thenReturn(body);
    when(body.getObject()).thenReturn(jsonObject);
    when(jsonObject.getJSONArray("weather")).thenReturn(jsonArray);
    when(jsonArray.getJSONObject(0)).thenReturn(jsonObject2);

    new WeatherClientUnmarshaller().unmarshallWeatherCode(response);

    verify(response).getBody();
    verify(body).getObject();
    verify(jsonObject).getJSONArray("weather");
    verify(jsonArray).getJSONObject(0);
    verify(jsonObject2).getInt("id");
  }
}