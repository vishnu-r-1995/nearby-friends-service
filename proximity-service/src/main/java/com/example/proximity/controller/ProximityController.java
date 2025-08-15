package com.example.proximity.controller;

import com.example.proximity.model.LocationMessage;
import com.example.proximity.service.ProximityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proximity")
public class ProximityController {

    private final ProximityService service;

    public ProximityController(ProximityService service) {
        this.service = service;
    }

    /**
     * Example: GET /proximity/nearby?userId=alice&radiusKm=5
     */
    @GetMapping("/nearby")
    public List<LocationMessage> getNearbyFriends(
            @RequestParam String userId,
            @RequestParam double radiusKm) {
        return service.findNearbyFriends(userId, radiusKm);
    }
}