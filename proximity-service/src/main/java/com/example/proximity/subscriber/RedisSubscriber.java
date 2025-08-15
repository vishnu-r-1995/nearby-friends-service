package com.example.proximity.subscriber;

import com.example.proximity.model.LocationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Store latest location per user
    private final Map<String, LocationMessage> latestLocations = new ConcurrentHashMap<>();

    public void handleMessage(String message) {
        try {
            LocationMessage location = objectMapper.readValue(message, LocationMessage.class);
            latestLocations.put(location.getUserId(), location);
            log.info("Updated location for user {} → ({}, {})",
                    location.getUserId(), location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            log.error("Failed to parse location message: {}", message, e);
        }
    }

    public Map<String, LocationMessage> getLatestLocations() {
        return latestLocations;
    }
}