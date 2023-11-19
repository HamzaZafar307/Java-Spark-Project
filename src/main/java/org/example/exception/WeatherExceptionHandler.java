package org.example.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeatherExceptionHandler {

    public static void handleWeatherException(WeatherException e) {
        // Handle the WeatherException, e.g., log the error
        log.error("WeatherException: " + e.getMessage());
    }

    public static void handleGeneralException(Exception e) {
        // Handle other general exceptions, e.g., log the error
        log.error("General Exception: " + e.getMessage());
    }
}
