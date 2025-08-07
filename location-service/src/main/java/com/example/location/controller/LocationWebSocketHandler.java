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
public class LocationWebSocketHandler implements WebSocketHandler {

    private final RedisPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            LocationUpdate update = objectMapper.readValue(message.getPayload(), LocationUpdate.class);
            log.info("Received location from {}: {}, {}", update.getUserId(), update.getLatitude(), update.getLongitude());
            publisher.publishLocation(update);
        } catch (Exception e) {
            log.error("Failed to process location message: {}", message.getPayload(), e);
        }
    }

    @Override public void afterConnectionEstablished(WebSocketSession session) {}
    @Override public void handleTransportError(WebSocketSession session, Throwable exception) {}
    @Override public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {}
    @Override public boolean supportsPartialMessages() { return false; }
}