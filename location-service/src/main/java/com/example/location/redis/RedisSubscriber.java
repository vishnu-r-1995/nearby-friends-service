package com.example.location.redis;

import com.example.location.model.LocationUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;

//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

//@Slf4j
@Component
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(RedisSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String payload = new String(message.getBody());
            LocationUpdate up = new LocationUpdate();
            up.getUserId();
            LocationUpdate update = objectMapper.readValue(payload, LocationUpdate.class);
            log.info("📩 Received from Redis - User: {}, Location: ({}, {})",
                    update.getUserId(), update.getLatitude(), update.getLongitude());
        } catch (Exception e) {
            log.error("Error while processing Redis message", e);
        }
    }

    // message payload will be raw String (unless you configure a serializer)
    public void handleMessage(String message) {
        log.info("Received message from Redis: {}", message);
        // parse JSON into LocationUpdate if needed (use Jackson)
    }
}