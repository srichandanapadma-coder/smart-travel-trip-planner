package com.smarttravel.smarttravel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarttravel.smarttravel.dto.RouteResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class RouteService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RouteService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public RouteResponse getRoute(
            String source,
            String destination) throws Exception {

        // 1. Convert source into coordinates
        JsonNode sourceLocation = geocode(source);

        // 2. Convert destination into coordinates
        JsonNode destinationLocation = geocode(destination);

        double sourceLatitude =
                sourceLocation.get("lat").asDouble();

        double sourceLongitude =
                sourceLocation.get("lon").asDouble();

        double destinationLatitude =
                destinationLocation.get("lat").asDouble();

        double destinationLongitude =
                destinationLocation.get("lon").asDouble();

        // 3. Ask OSRM for the road route
        String routeUrl =
                "https://router.project-osrm.org/route/v1/driving/"
                + sourceLongitude + "," + sourceLatitude
                + ";"
                + destinationLongitude + "," + destinationLatitude
                + "?overview=false";

        String routeResponse =
                restTemplate.getForObject(
                        routeUrl,
                        String.class
                );

        JsonNode routeJson =
                objectMapper.readTree(routeResponse);

        if (!"Ok".equals(routeJson.get("code").asText())) {
            throw new RuntimeException(
                    "Route could not be found"
            );
        }

        JsonNode route =
                routeJson.get("routes").get(0);

        // OSRM distance is in meters
        double distanceKm =
                route.get("distance").asDouble() / 1000;

        // OSRM duration is in seconds
        double durationMinutes =
                route.get("duration").asDouble() / 60;

        return new RouteResponse(
                source,
                destination,
                distanceKm,
                durationMinutes,
                sourceLatitude,
                sourceLongitude,
                destinationLatitude,
                destinationLongitude
        );
    }

    private JsonNode geocode(String place) throws Exception {

        String encodedPlace =
                URLEncoder.encode(
                        place,
                        StandardCharsets.UTF_8
                );

        String url =
                "https://nominatim.openstreetmap.org/search"
                + "?q=" + encodedPlace
                + "&format=jsonv2"
                + "&limit=1";

        var request =
                org.springframework.http.RequestEntity
                        .get(url)
                        .header(
                                "User-Agent",
                                "SmartTravelTripPlanner/1.0"
                        )
                        .build();

        var response =
                restTemplate.exchange(
                        request,
                        String.class
                );

        JsonNode results =
                objectMapper.readTree(
                        response.getBody()
                );

        if (!results.isArray() ||
                results.isEmpty()) {

            throw new RuntimeException(
                    "Location not found: " + place
            );
        }

        return results.get(0);
    }
}