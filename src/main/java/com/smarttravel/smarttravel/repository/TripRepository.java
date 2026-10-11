package com.smarttravel.smarttravel.repository;

import com.smarttravel.smarttravel.entity.Trip;
import com.smarttravel.smarttravel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUser(User user);
}