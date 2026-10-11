
package com.smarttravel.smarttravel.planner;

import java.util.ArrayList;
import java.util.List;

public class PlannerRequest {

    private String source;
    private String destination;
    private String startDate;
    private String endDate;

    private int travelers;
    private double budget;

    // Supports the older frontend that sends one interest string
    private String interest;

    // Main transport used to reach the destination
    // Examples: BUS, TRAIN, FLIGHT, OWN_VEHICLE
    private String arrivalTransport;

    // Transport used to travel around the destination
    // Examples: RECOMMEND, PUBLIC_TRANSPORT, TAXI,
    // RENTAL_SCOOTER, RENTAL_CAR, OWN_VEHICLE
    private String localTransport;

    // Supports any number of selected interests
    private List<String> interests = new ArrayList<>();

    // Default constructor required for JSON request conversion
    public PlannerRequest() {
    }

    // Source
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // Destination
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    // Start date
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    // End date
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Number of travelers
    public int getTravelers() {
        return travelers;
    }

    public void setTravelers(int travelers) {
        this.travelers = travelers;
    }

    // Total budget for the trip
    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    // Single interest for backward compatibility
    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    // Arrival transport
    public String getArrivalTransport() {
        return arrivalTransport;
    }

    public void setArrivalTransport(String arrivalTransport) {
        this.arrivalTransport = arrivalTransport;
    }

    // Local transport
    public String getLocalTransport() {
        return localTransport;
    }

    public void setLocalTransport(String localTransport) {
        this.localTransport = localTransport;
    }

    // Multiple interests
    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        if (interests == null) {
            this.interests = new ArrayList<>();
        } else {
            this.interests = new ArrayList<>(interests);
        }
    }
}
