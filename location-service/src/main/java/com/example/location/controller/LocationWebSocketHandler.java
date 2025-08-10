package com.example.location.controller;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.location.model.LocationUpdate;
import com.example.location.redis.RedisPublisher;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private final RedisPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connection established: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // parse into LocationUpdate (ensure model has getters/setters or lombok)
        LocationUpdate update = objectMapper.readValue(payload, LocationUpdate.class);
        log.info("Received update from user {}: {}, {}", update.getUserId(), update.getLatitude(), update.getLongitude());

        // publish to redis topic
        publisher.publish("location-updates", payload);
    }

    // @Override
    // public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    //     log.info("WebSocket connection closed: {}", session.getId());
    // }  
}