package com.smarttravel.smarttravel.dto;

public class RouteResponse {

    private String source;
    private String destination;

    private double distanceKm;
    private double durationMinutes;

    private double sourceLatitude;
    private double sourceLongitude;

    private double destinationLatitude;
    private double destinationLongitude;

    public RouteResponse() {
    }

    public RouteResponse(
            String source,
            String destination,
            double distanceKm,
            double durationMinutes,
            double sourceLatitude,
            double sourceLongitude,
            double destinationLatitude,
            double destinationLongitude) {

        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.sourceLatitude = sourceLatitude;
        this.sourceLongitude = sourceLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getDurationMinutes() {
        return durationMinutes;
    }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }
}