package org.example.controller;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;


import org.example.model.WeatherDetails;
import org.example.service.WeatherDetailsService;
import org.example.util.*;

import static spark.Spark.*;

public class WeatherDetailsController {

    private final WeatherDetailsService weatherDetailsService;

    public WeatherDetailsController(WeatherDetailsService weatherDetailsService) {
        this.weatherDetailsService = weatherDetailsService;
        setupRoutes();
    }

    private void setupRoutes() {
        // Create
        post("/weather", this::createWeatherDetails, new JsonTransformer());

        // Read (Get all)
        get("/weather", this::getAllWeatherDetails, new JsonTransformer());

        // Read (Get by ID)
        get("/weather/:id", this::getWeatherDetailsById, new JsonTransformer());

        // Update
        put("/weather/:id", this::updateWeatherDetails, new JsonTransformer());

        // Delete
        delete("/weather/:id", this::deleteWeatherDetails, new JsonTransformer());

        get("/hello", (req, res) -> "Hello, Spark!");
    }

    private Object createWeatherDetails(Request request, Response response) {
        WeatherDetails weatherDetails = new Gson().fromJson(request.body(), WeatherDetails.class);
        WeatherDetails savedWeatherDetails = weatherDetailsService.saveWeatherDetails(weatherDetails);
        response.status(201); // Created
        return savedWeatherDetails;
    }

    private Object getAllWeatherDetails(Request request, Response response) {
        return weatherDetailsService.getAllWeatherDetails();
    }

    private Object getWeatherDetailsById(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        WeatherDetails weatherDetails = weatherDetailsService.getWeatherDetailsById(id);
        if (weatherDetails != null) {
            return weatherDetails;
        } else {
            response.status(404); // Not Found
            return "WeatherDetails not found";
        }
    }

    private Object updateWeatherDetails(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        WeatherDetails existingWeatherDetails = weatherDetailsService.getWeatherDetailsById(id);

        if (existingWeatherDetails != null) {
            WeatherDetails updatedWeatherDetails = new Gson().fromJson(request.body(), WeatherDetails.class);
            updatedWeatherDetails.setId(existingWeatherDetails.getId());
            WeatherDetails savedWeatherDetails = weatherDetailsService.saveWeatherDetails(updatedWeatherDetails);
            return savedWeatherDetails;
        } else {
            response.status(404); // Not Found
            return "WeatherDetails not found";
        }
    }

    private Object deleteWeatherDetails(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        WeatherDetails existingWeatherDetails = weatherDetailsService.getWeatherDetailsById(id);

        if (existingWeatherDetails != null) {
            weatherDetailsService.deleteWeatherDetails(id);
            return "WeatherDetails deleted successfully";
        } else {
            response.status(404); // Not Found
            return "WeatherDetails not found";
        }
    }
}
