package org.example.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class WeatherDetails {
    private Long id;
    private String city;
    private String description;
    private double temperature;

    private long timestamp;

}
