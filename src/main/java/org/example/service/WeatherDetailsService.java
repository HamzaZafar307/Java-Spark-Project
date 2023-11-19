package org.example.service;

import java.util.*;
;
import java.util.concurrent.*;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.example.controller.UserController;
import org.example.exception.WeatherException;
import org.example.model.User;
import org.example.model.WeatherDetails;

@Slf4j
public class WeatherDetailsService {

    //private final Map<Long, WeatherDetails> weatherDetailsMap = new HashMap<>();
    private static long nextId = 1;

    private  Map<Long, WeatherDetails> weatherDetailsMap = new ConcurrentHashMap<>();
    private List<String> cities= new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private UserController userController;

    private UserService userService;
    public WeatherDetailsService() {
        // Initialize your map with some initial data if needed

         userService= new UserService();
         userController = new UserController(userService);
        // Schedule the task to update weather details every minute
        scheduler.scheduleAtFixedRate(this::updateWeatherDetailsFromApi, 0, 1, TimeUnit.MINUTES);
    }

    public List<String> getAllRegisteredCities(){
        return weatherDetailsMap.values().stream()
                .map(WeatherDetails::getCity)
                .distinct()
                .collect(Collectors.toList());
    }
    private void updateWeatherDetailsFromApi() {
        WeatherApiClient weatherApiClient = new WeatherApiClient();
        List<String> cities = getAllRegisteredCities();
        if(cities.isEmpty()){
        cities.add("Berlin");
        }
        for(String city: cities){
            WeatherDetails latestWeatherDetails = weatherApiClient.getUpdatedDetailsOfWeather(city);

            if (latestWeatherDetails != null) {
                long timestamp = System.currentTimeMillis();

                // Update the weather details map with the latest data
                weatherDetailsMap.put(timestamp, latestWeatherDetails);
                checkWeatherNotifications(latestWeatherDetails);
                // Log the update
                log.info("Weather details updated at " + timestamp);
            } else {
                log.error("Failed to update weather details from API.");
                throw new WeatherException("Failed to update weather details from API.");
            }
        }
    }

    private void checkWeatherNotifications(WeatherDetails latestWeatherDetails) {
        // Iterate through each user
        for (User user : userService.getAllUsers()) {
            // Check if the user has subscribed to alerts
            if (user.isSubscribedUser()) {
                // Check if the latest weather details match the user's subscribed city
                if (user.getSubscribedCities().contains(latestWeatherDetails.getCity())) {
                    // Check if app_temp > threshold_temperature
                    if (latestWeatherDetails.getTemperature() > user.getTemperatureThreshold()) {
                        // Log a notification (you can replace this with actual notification logic)
                        log.info("Notification: High temperature alert for user " + user.getName() +
                                " in city " + latestWeatherDetails.getCity() + ". Current temperature: " +
                                latestWeatherDetails.getTemperature() + "°C");
                    }
                }
            }
        }
    }

    public WeatherDetails saveWeatherDetails(WeatherDetails weatherDetails) {
        if (weatherDetails.getId() == null ) {
            if(nextId != 1L)
            weatherDetails.setId(nextId++);
            else weatherDetails.setId(nextId);
        }
        weatherDetailsMap.put(weatherDetails.getId(), weatherDetails);
        return weatherDetails;
    }

    public List<WeatherDetails> getAllWeatherDetails() {
        return new ArrayList<>(weatherDetailsMap.values());
    }

    // Method to get the most recent weather details by city (case-insensitive)
    public Optional<WeatherDetails> getWeatherDetailsByCity(String cityName) {
        return weatherDetailsMap.values().stream()
                .filter(weatherDetails -> weatherDetails.getCity().equalsIgnoreCase(cityName))
                .max(Comparator.comparingLong(WeatherDetails::getTimestamp));
    }
    public WeatherDetails getWeatherDetailsById(Long id) {
        return weatherDetailsMap.get(id);
    }

    public void deleteWeatherDetails(Long id) {
        weatherDetailsMap.remove(id);
    }
}
