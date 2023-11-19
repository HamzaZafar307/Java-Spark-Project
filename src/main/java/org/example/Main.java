package org.example;

import org.example.controller.UserController;
import org.example.controller.WeatherDetailsController;
import org.example.service.UserService;
import org.example.service.WeatherDetailsService;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(8085);

       // get("/hello", (req, res) -> "Hello, Spark!");
       // port(4567);

        // Instantiate services and controllers
        WeatherDetailsService weatherDetailsService = new WeatherDetailsService();
        WeatherDetailsController weatherDetailsController = new WeatherDetailsController(weatherDetailsService);

        // Define additional controllers and services as needed

        // Ensure that you have routes defined in your controllers

        // The following line is optional but can be used to serve static files (e.g., HTML, CSS, JS)
       // staticFiles.location("/public");

        // Start the Spark server
        init();
    }
}

