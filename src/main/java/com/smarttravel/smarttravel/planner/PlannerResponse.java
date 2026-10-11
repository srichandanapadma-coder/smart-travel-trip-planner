
package com.smarttravel.smarttravel.planner;

import java.util.ArrayList;
import java.util.List;

public class PlannerResponse {

    // Basic trip details
    private String source;
    private String destination;
    private int days;
    private int travelers;

    // User's budget
    private double totalBudget;
    private double budgetPerPerson;

    // Selected interests and daily itinerary
    private String interest;
    private List<String> itinerary = new ArrayList<>();

    // Response message
    private String message;

    // Transport details
    private String arrivalTransport;
    private String localTransport;
    private String transportAdvice;
    private String mainRouteUrl;

    // Estimated expenses
    private double transportCost;
    private double accommodationCost;
    private double foodCost;
    private double localTravelCost;
    private double activityCost;

    // Total estimated expenses
    private double estimatedTotalCost;
    private double estimatedCostPerPerson;

    // Budget status and advice
    private boolean withinBudget;
    private String budgetAdvice;

    // Constructor used by PlannerService
    public PlannerResponse(
            String source,
            String destination,
            int days,
            int travelers,
            double totalBudget,
            double budgetPerPerson,
            String interest,
            List<String> itinerary,
            String message) {

        this.source = source;
        this.destination = destination;
        this.days = days;
        this.travelers = travelers;
        this.totalBudget = totalBudget;
        this.budgetPerPerson = budgetPerPerson;
        this.interest = interest;

        this.itinerary = itinerary != null
                ? new ArrayList<>(itinerary)
                : new ArrayList<>();

        this.message = message;
    }

    // Source
    public String getSource() {
        return source;
    }

    // Destination
    public String getDestination() {
        return destination;
    }

    // Number of trip days
    public int getDays() {
        return days;
    }

    // Number of travelers
    public int getTravelers() {
        return travelers;
    }

    // Total budget entered by the user
    public double getTotalBudget() {
        return totalBudget;
    }

    // Budget per person entered by the user
    public double getBudgetPerPerson() {
        return budgetPerPerson;
    }

    // Selected interests
    public String getInterest() {
        return interest;
    }

    // Daily itinerary
    public List<String> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary != null
                ? new ArrayList<>(itinerary)
                : new ArrayList<>();
    }

    // Response message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    // Transport advice
    public String getTransportAdvice() {
        return transportAdvice;
    }

    public void setTransportAdvice(String transportAdvice) {
        this.transportAdvice = transportAdvice;
    }

    // Google Maps route link
    public String getMainRouteUrl() {
        return mainRouteUrl;
    }

    public void setMainRouteUrl(String mainRouteUrl) {
        this.mainRouteUrl = mainRouteUrl;
    }

    // Main journey transport cost
    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    // Accommodation cost
    public double getAccommodationCost() {
        return accommodationCost;
    }

    public void setAccommodationCost(double accommodationCost) {
        this.accommodationCost = accommodationCost;
    }

    // Food cost
    public double getFoodCost() {
        return foodCost;
    }

    public void setFoodCost(double foodCost) {
        this.foodCost = foodCost;
    }

    // Local travel cost
    public double getLocalTravelCost() {
        return localTravelCost;
    }

    public void setLocalTravelCost(double localTravelCost) {
        this.localTravelCost = localTravelCost;
    }

    // Activities and sightseeing cost
    public double getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(double activityCost) {
        this.activityCost = activityCost;
    }

    // Total estimated trip cost
    public double getEstimatedTotalCost() {
        return estimatedTotalCost;
    }

    public void setEstimatedTotalCost(double estimatedTotalCost) {
        this.estimatedTotalCost = estimatedTotalCost;
    }

    // Estimated cost per traveler
    public double getEstimatedCostPerPerson() {
        return estimatedCostPerPerson;
    }

    public void setEstimatedCostPerPerson(
            double estimatedCostPerPerson) {
        this.estimatedCostPerPerson = estimatedCostPerPerson;
    }

    // Whether the estimate fits the user's budget
    public boolean isWithinBudget() {
        return withinBudget;
    }

    public void setWithinBudget(boolean withinBudget) {
        this.withinBudget = withinBudget;
    }

    // Advice based on the budget
    public String getBudgetAdvice() {
        return budgetAdvice;
    }

    public void setBudgetAdvice(String budgetAdvice) {
        this.budgetAdvice = budgetAdvice;
    }
}
