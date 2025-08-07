package com.example.location.redis;

import com.example.location.model.LocationUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CHANNEL = "location-updates";

    public void publishLocation(LocationUpdate update) {
        try {
            String json = objectMapper.writeValueAsString(update);
            redisTemplate.convertAndSend(CHANNEL, json);
            log.info("Published to Redis channel {}: {}", CHANNEL, json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing location update", e);
        }
    }
}