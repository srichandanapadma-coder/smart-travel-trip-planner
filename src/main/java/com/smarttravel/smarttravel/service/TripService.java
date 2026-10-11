package com.smarttravel.smarttravel.service;

import com.smarttravel.smarttravel.entity.Trip;
import com.smarttravel.smarttravel.entity.User;
import com.smarttravel.smarttravel.repository.TripRepository;
import com.smarttravel.smarttravel.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(
            TripRepository tripRepository,
            UserRepository userRepository) {

        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public Trip createTrip(Trip trip, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        trip.setUser(user);

        return tripRepository.save(trip);
    }

    public List<Trip> getMyTrips(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return tripRepository.findByUser(user);
    }
}