package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.model.WeatherDetails;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WeatherApiClient {

    private static final String API_KEY = "a7b147018e224e41935b9eb4b51d84aa";
    private static final String API_ENDPOINT = "https://api.weatherbit.io/v2.0/current?";

    public String getWeatherData(String city) {
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(API_ENDPOINT + "city=" + city+ "&key=" + API_KEY);

        try {
            org.apache.http.HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                log.error("Error: " + response.getStatusLine().getStatusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WeatherDetails getUpdatedDetailsOfWeather(String city){
        String jsonWeatherData = getWeatherData(city);
        if (jsonWeatherData != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonWeatherData, JsonObject.class);

            // Extract "data" array from JSON
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            if (dataArray.size() > 0) {
                // Extract first object from "data" array
                JsonObject firstObject = dataArray.get(0).getAsJsonObject();

                // Extract specific attributes from the object
                double appTemp = firstObject.get("app_temp").getAsDouble();
                String cityName = firstObject.get("city_name").getAsString();


                // Create a WeatherDetails object with the extracted information
                WeatherDetails weatherDetails = new WeatherDetails();
                weatherDetails.setTemperature(appTemp);
                weatherDetails.setCity(cityName);
                weatherDetails.setTimestamp(System.currentTimeMillis());

                return weatherDetails;
            } else {
                log.error("No data found in the 'data' array.");
                return null;
            }
        } else {
            log.error("Failed to fetch weather data.");
            return null;
        }
    }

    public Map<String, WeatherDetails> getWeatherDetailsForCities(List<String> cities) {
        Map<String, WeatherDetails> weatherDetailsMap = new HashMap<>();

        for (String city : cities) {
            WeatherDetails weatherDetails = getUpdatedDetailsOfWeather(city);
            if (weatherDetails != null) {
                weatherDetailsMap.put(city, weatherDetails);
            } else {
                log.error("Failed to fetch weather details for city: " + city);
            }
        }

        return weatherDetailsMap;
    }
    public static void main(String[] args) {
 //       WeatherApiClient weatherApiClient = new WeatherApiClient();
  //      weatherApiClient.getUpdatedDetailsOfWeather();
//        if (weatherData != null) {
//            System.out.println("Weather Data: " + weatherData);
//            // Parse the JSON response or process the data as needed
//        } else {
//            System.err.println("Failed to fetch weather data.");
//        }
    }
}
