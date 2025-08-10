package com.example.location.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CHANNEL = "location-updates";

    @Autowired
    public RedisPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String topic, String message) {
        log.info("Publishing to {}: {}", topic, message);
        redisTemplate.convertAndSend(topic, message);
    }
}