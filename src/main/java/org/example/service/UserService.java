package org.example.service;

import org.example.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {

    private final Map<Long, User> userMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User createUser(String name, List<String> subscribedCities, double temperatureThreshold) {
        long userId = idGenerator.getAndIncrement();
        User user = new User(userId, name, subscribedCities, temperatureThreshold,true);
        userMap.put(userId, user);
        return user;
    }

    public User getUserById(long userId) {
        return userMap.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void updateUser(long userId, String name, List<String> subscribedCities, double temperatureThreshold) {
        User user = userMap.get(userId);
        if (user != null) {
            user.setName(name);
            user.setSubscribedCities(subscribedCities);
            user.setTemperatureThreshold(temperatureThreshold);
        }
    }

    public void unsubscribeUser(long userId) {
        User user = userMap.get(userId);
        if (user != null) {
            user.setSubscribedUser(false);
        }
    }

    public void deleteUser(long userId) {
        userMap.remove(userId);
    }
}
