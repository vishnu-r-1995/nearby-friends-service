package com.example.proximity.subscriber;

import com.example.proximity.model.LocationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    // Store latest location per user
    private final ConcurrentHashMap<String, LocationMessage> locationMap = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate redisTemplate;

    // public void handleMessage(String message) {
    //     try {
    //         LocationMessage location = objectMapper.readValue(message, LocationMessage.class);
    //         latestLocations.put(location.getUserId(), location);
    //         log.info("Updated location for user {} → ({}, {})",
    //                 location.getUserId(), location.getLatitude(), location.getLongitude());
    //     } catch (Exception e) {
    //         log.error("Failed to parse location message: {}", message, e);
    //     }
    // }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = message.toString();
            LocationMessage update = objectMapper.readValue(payload, LocationMessage.class);

            // Update in-memory cache
            locationMap.put(update.getUserId(), update);

            // Store in Redis for persistence
            String redisKey = "location:" + update.getUserId();
            redisTemplate.opsForHash().put(redisKey, "latitude", String.valueOf(update.getLatitude()));
            redisTemplate.opsForHash().put(redisKey, "longitude", String.valueOf(update.getLongitude()));

            log.info("Updated location for user {} → ({}, {}) [Saved to Redis & Memory]",
                     update.getUserId(), update.getLatitude(), update.getLongitude());

        } catch (Exception e) {
            log.error("Error processing location update", e);
        }
    }

    @PostConstruct
    public void loadFromRedisOnStartup() {
        log.info("Loading existing locations from Redis...");
        Set<String> keys = redisTemplate.keys("location:*");
        if (keys != null) {
            for (String key : keys) {
                Map<Object, Object> hash = redisTemplate.opsForHash().entries(key);
                if (hash.containsKey("latitude") && hash.containsKey("longitude")) {
                    String userId = key.substring("location:".length());
                    double lat = Double.parseDouble(hash.get("latitude").toString());
                    double lon = Double.parseDouble(hash.get("longitude").toString());
                    locationMap.put(userId, new LocationMessage(userId, lat, lon));
                    log.info("Loaded from Redis: {} → ({}, {})", userId, lat, lon);
                }
            }
        }
    }

    public Map<String, LocationMessage> getLatestLocations() {
        return locationMap;
    }
}