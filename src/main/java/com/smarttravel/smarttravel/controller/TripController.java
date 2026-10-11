package com.smarttravel.smarttravel.controller;

import com.smarttravel.smarttravel.entity.Trip;
import com.smarttravel.smarttravel.service.TripService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(
            @RequestBody Trip trip,
            Authentication authentication) {

        Trip savedTrip = tripService.createTrip(
                trip,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTrip);
    }

    @GetMapping("/my-trips")
    public ResponseEntity<List<Trip>> getMyTrips(
            Authentication authentication) {

        return ResponseEntity.ok(
                tripService.getMyTrips(
                        authentication.getName()
                )
        );
    }
}