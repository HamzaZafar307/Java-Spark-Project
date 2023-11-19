package org.example.controller;

import com.google.gson.Gson;
import org.example.model.User;
import org.example.service.UserService;

import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import org.example.util.*;
import java.util.List;

@Slf4j
public class UserController {

    private UserService userService ;

    public UserController(UserService userService) {
        this.userService = userService;
        setupRoutes();
    }

    private void setupRoutes() {
        // Create
        Spark.post("/api/users", createUser, new JsonTransformer());

        // Read (Get all)
        Spark.get("/api/users", getAllUsers, new JsonTransformer());

        // Read (Get by ID)
        Spark.get("/api/users/:id", getUserById, new JsonTransformer());

        // Update
        Spark.put("/api/users/:id", updateUser, new JsonTransformer());

        // Delete
        Spark.delete("/api/users/:id", deleteUser, new JsonTransformer());

        Spark.put("/api/users/unsubscribe/:id", unsubscribeUser, new JsonTransformer());
    }

    private final Route createUser = (Request request, Response response) -> {
        try {
            // Parse the JSON data from the request body into a User object
            User newUser = new Gson().fromJson(request.body(), User.class);

            // Call the service to save the User
            User savedUser = userService.createUser(newUser.getName(), newUser.getSubscribedCities(), newUser.getTemperatureThreshold());

            response.status(201); // Created
            return new JsonTransformer().render(savedUser);
        } catch (Exception e) {
            log.error("Error creating user", e);
            response.status(500);
            return "Error creating user";
        }
    };

    public final Route getAllUsers = (Request request, Response response) -> {
        List<User> users = userService.getAllUsers();
        return new JsonTransformer().render(users);
    };

    private final Route getUserById = (Request request, Response response) -> {
        long userId = Long.parseLong(request.params(":id"));
        User user = userService.getUserById(userId);
        if (user != null) {
            return new JsonTransformer().render(user);
        } else {
            response.status(404);
            return "User not found";
        }
    };

    private final Route updateUser = (Request request, Response response) -> {
        long userId = Long.parseLong(request.params(":id"));
        try {
            // Parse the JSON data from the request body into a User object
            User updatedUser = new Gson().fromJson(request.body(), User.class);

            // Call the service to update the User
            userService.updateUser(userId, updatedUser.getName(), updatedUser.getSubscribedCities(), updatedUser.getTemperatureThreshold());

            return "User updated successfully";
        } catch (Exception e) {
            log.error("Error updating user", e);
            response.status(500);
            return "Error updating user";
        }
    };

    private final Route unsubscribeUser = (Request request, Response response) -> {
        long userId = Long.parseLong(request.params(":id"));
        try {
            // Parse the JSON data from the request body into a User object
            User updatedUser = new Gson().fromJson(request.body(), User.class);

            // Call the service to update the User
            userService.unsubscribeUser(userId);

            return "User unsubscribed successfully";
        } catch (Exception e) {
            log.error("Error unsubscribing user", e);
            response.status(500);
            return "Error updating user";
        }
    };

    private final Route deleteUser = (Request request, Response response) -> {
        long userId = Long.parseLong(request.params(":id"));
        userService.deleteUser(userId);
        return "User deleted successfully";
    };


}

