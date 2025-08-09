package com.example.location.controller;

import com.example.location.model.LocationUpdate;
import com.example.location.redis.RedisPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocationWebSocketHandler extends TextWebSocketHandler {

    private final RedisPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connection established: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            LocationUpdate update = objectMapper.readValue(message.getPayload(), LocationUpdate.class);
            log.info("Received location update from {}: ({}, {})",
                    update.getUserId(), update.getLatitude(), update.getLongitude());

            // Publish to Redis so other services can process
            publisher.publish("location-updates", message.getPayload());
        } catch (Exception e) {
            log.error("Error processing WebSocket message", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("WebSocket connection closed: {}", session.getId());
    }  
}