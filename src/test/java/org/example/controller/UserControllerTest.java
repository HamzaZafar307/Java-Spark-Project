package org.example.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.example.model.User;
import org.example.service.UserService;
import org.example.util.JsonTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Hamza1");
        user1.setSubscribedCities(Arrays.asList("Berlin"));
        user1.setTemperatureThreshold(25.0);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Hamza2");
        user2.setSubscribedCities(Arrays.asList("Berlin"));
        user2.setTemperatureThreshold(20.0);

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        Request request = mock(Request.class);
        Response response = mock(Response.class);

        // Act
        String result = (String) userController.getAllUsers.handle(request, response);


        assertNotNull(result);
        assertTrue(result.contains("Hamza")); // Check for user names in the JSON response
        assertTrue(result.contains("Hamza1"));
        // Add more assertions based on your actual JSON structure
    }

}
