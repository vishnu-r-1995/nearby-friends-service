package com.example.proximity.model;

import lombok.Data;

@Data
public class LocationMessage {
    private String userId;
    private double latitude;
    private double longitude;
    private long timestamp;
}