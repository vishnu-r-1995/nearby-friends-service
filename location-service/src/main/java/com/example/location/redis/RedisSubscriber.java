package com.example.location.redis;

import com.example.location.model.LocationUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody());
            LocationUpdate update = objectMapper.readValue(payload, LocationUpdate.class);
            log.info("📩 Received from Redis - User: {}, Location: ({}, {})",
                    update.getUserId(), update.getLatitude(), update.getLongitude());
        } catch (Exception e) {
            log.error("Error while processing Redis message", e);
        }
    }
}