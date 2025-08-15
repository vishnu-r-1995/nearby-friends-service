package com.example.proximity.service;

import com.example.proximity.model.LocationMessage;
import com.example.proximity.subscriber.RedisSubscriber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProximityService {

    private final RedisSubscriber subscriber;

    public ProximityService(RedisSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * Find friends within given radius (in km) from a given user
     */
    public List<LocationMessage> findNearbyFriends(String userId, double radiusKm) {
        Map<String, LocationMessage> allLocations = subscriber.getLatestLocations();

        LocationMessage currentUserLocation = allLocations.get(userId);
        if (currentUserLocation == null) {
            return List.of(); // user location not known
        }

        return allLocations.values().stream()
                .filter(loc -> !loc.getUserId().equals(userId)) // exclude self
                .filter(loc -> distanceInKm(
                        currentUserLocation.getLatitude(),
                        currentUserLocation.getLongitude(),
                        loc.getLatitude(),
                        loc.getLongitude()) <= radiusKm)
                .collect(Collectors.toList());
    }

    /**
     * Haversine formula to calculate distance between two lat/lon points in km
     */
    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}