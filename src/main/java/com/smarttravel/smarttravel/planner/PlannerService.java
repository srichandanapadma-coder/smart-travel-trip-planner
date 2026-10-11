
package com.smarttravel.smarttravel.planner;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class PlannerService {

    public PlannerResponse planTrip(PlannerRequest request) {

        // 1. Validate required inputs
        if (request == null
                || request.getSource() == null
                || request.getSource().isBlank()
                || request.getDestination() == null
                || request.getDestination().isBlank()) {
            throw new IllegalArgumentException(
                    "Source and destination are required.");
        }

        if (request.getStartDate() == null
                || request.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required.");
        }

        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(request.getStartDate());
            end = LocalDate.parse(request.getEndDate());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Dates must use YYYY-MM-DD format.");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }

        int travelers = request.getTravelers();
        double budget = request.getBudget();

        if (travelers < 1) {
            throw new IllegalArgumentException(
                    "At least one traveler is required.");
        }

        if (!Double.isFinite(budget) || budget <= 0) {
            throw new IllegalArgumentException(
                    "Enter a valid budget greater than zero.");
        }

        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;

        String source = request.getSource().trim();
        String destination = request.getDestination().trim();

        String arrival = normalize(request.getArrivalTransport());
        String local = normalize(request.getLocalTransport());

        // 2. Read selected interests
        List<String> interests = new ArrayList<>();

        if (request.getInterests() != null) {
            for (String item : request.getInterests()) {
                if (item != null && !item.isBlank()) {
                    interests.add(item.trim());
                }
            }
        }

        if (interests.isEmpty()
                && request.getInterest() != null
                && !request.getInterest().isBlank()) {
            interests.addAll(
                    Arrays.asList(request.getInterest().split(","))
            );
        }

        interests.removeIf(item -> item == null || item.isBlank());

        String selectedInterests = interests.isEmpty()
                ? "general sightseeing"
                : String.join(", ", interests);

        // 3. Build an itinerary emphasizing free and low-cost activities
        List<String> itinerary = buildItinerary(
                destination, start, days, interests
        );

        String transportAdvice = buildTransportAdvice(arrival, local);
        String routeUrl = buildRouteUrl(source, destination, arrival);

        /*
         * 4. Budget allocation
         *
         * These values are spending limits, NOT live prices.
         * Reserve 5% for unexpected costs.
         * Allocate the remaining 95% across the five categories.
         */

        double reserve = roundMoney(budget * 0.05);
        double availableForCategories = roundMoney(budget - reserve);

        double transportPercentage;

        if (arrival.equals("flight")) {
            transportPercentage = 0.35;
        } else if (arrival.equals("train")) {
            transportPercentage = 0.20;
        } else if (arrival.equals("bus")) {
            transportPercentage = 0.15;
        } else {
            transportPercentage = 0.25;
        }

        double transportCost = roundMoney(
                availableForCategories * transportPercentage
        );

        double accommodationCost = roundMoney(
                availableForCategories * 0.25
        );

        double foodCost = roundMoney(
                availableForCategories * 0.20
        );

        double localPercentage =
                local.equals("taxi")
                || local.equals("cab")
                || local.equals("rental_car")
                || local.equals("rental_scooter")
                ? 0.10 : 0.08;

        double localTravelCost = roundMoney(
                availableForCategories * localPercentage
        );

        // Activities can use only what remains after the other categories.
        double activityCost = Math.max(
                0,
                roundMoney(
                        availableForCategories
                        - transportCost
                        - accommodationCost
                        - foodCost
                        - localTravelCost
                )
        );

        // 5. Verify the total. Never silently report an over-budget allocation.
        double estimatedTotal = roundMoney(
                transportCost
                + accommodationCost
                + foodCost
                + localTravelCost
                + activityCost
        );

        if (estimatedTotal > budget) {
            double excess = roundMoney(estimatedTotal - budget);

            activityCost = roundMoney(
                    Math.max(0, activityCost - excess)
            );

            estimatedTotal = roundMoney(
                    transportCost
                    + accommodationCost
                    + foodCost
                    + localTravelCost
                    + activityCost
            );
        }

        if (estimatedTotal > budget) {
            throw new IllegalStateException(
                    "Could not create a budget-capped expense allocation."
            );
        }

        double estimatedPerPerson = roundMoney(
                estimatedTotal / travelers
        );

        boolean withinBudget = estimatedTotal <= budget;

        double remaining = roundMoney(budget - estimatedTotal);

        String budgetAdvice = String.format(
                Locale.US,
                "Group budget: Rs. %.2f. Allocated spending limit: Rs. %.2f. "
                + "Unallocated buffer: Rs. %.2f. "
                + "Keep every booking within its category limit. "
                + "These allocations are not verified market prices. "
                + "If actual fares or accommodation exceed these limits, "
                + "choose cheaper options or revise the trip before booking.",
                budget,
                estimatedTotal,
                remaining
        );

        String message =
                "Your itinerary uses predefined destination rules and "
                + "budget-based spending limits. Prices are not live "
                + "quotations, so actual affordability has not been verified.";

        // 6. Construct the existing response DTO
        PlannerResponse response = new PlannerResponse(
                source,
                destination,
                days,
                travelers,
                budget,
                budget / travelers,
                selectedInterests,
                itinerary,
                message
        );

        response.setArrivalTransport(arrival);
        response.setLocalTransport(local);
        response.setTransportAdvice(transportAdvice);
        response.setMainRouteUrl(routeUrl);

        response.setTransportCost(transportCost);
        response.setAccommodationCost(accommodationCost);
        response.setFoodCost(foodCost);
        response.setLocalTravelCost(localTravelCost);
        response.setActivityCost(activityCost);

        response.setEstimatedTotalCost(estimatedTotal);
        response.setEstimatedCostPerPerson(estimatedPerPerson);
        response.setWithinBudget(withinBudget);
        response.setBudgetAdvice(budgetAdvice);

        return response;
    }

    // --------------------------------------------------
    // ITINERARY GENERATION
    // --------------------------------------------------

    private List<String> buildItinerary(
            String destination,
            LocalDate start,
            int days,
            List<String> interests) {

        List<String> activities = new ArrayList<>();
        String place = destination.toLowerCase(Locale.ROOT);

        if (place.contains("goa")) {

            if (matches(interests, "beaches", "beach", "relaxation")) {
                activities.add(
                        "Visit a public beach such as Baga or Calangute. "
                        + "Prefer free beach activities and check transport costs."
                );
            }

            if (matches(interests, "history", "heritage")) {
                activities.add(
                        "Explore Old Goa's heritage areas. Check entry fees "
                        + "and travel costs before visiting paid attractions."
                );
            }

            if (matches(interests, "nature")) {
                activities.add(
                        "Explore a public scenic area or nature spot "
                        + "with little or no entry fee."
                );
            }

            if (matches(interests, "food")) {
                activities.add(
                        "Choose affordable local eateries and stay within "
                        + "your food allowance."
                );
            }

            if (matches(interests, "adventure", "water sports")) {
                activities.add(
                        "Consider water sports only if a verified price "
                        + "fits your remaining activity allowance; otherwise "
                        + "choose a free beach activity."
                );
            }

            if (matches(interests, "nightlife")) {
                activities.add(
                        "Explore a public evening area or market. "
                        + "Avoid paid venues if they exceed your budget."
                );
            }

            if (matches(interests, "boat", "boating")) {
                activities.add(
                        "Check boat-trip prices before committing. "
                        + "Choose a free waterfront visit if costs are too high."
                );
            }

            if (matches(interests, "spiritual", "spiritual places")) {
                activities.add(
                        "Visit a local church or temple and check "
                        + "transport costs in advance."
                );
            }

        } else if (place.contains("hyderabad")) {

            if (matches(interests, "history", "heritage")) {
                activities.add(
                        "Explore the Charminar area and old city. "
                        + "Check any attraction entry fees."
                );
            }

            if (matches(interests, "food")) {
                activities.add(
                        "Try affordable local food within your daily food limit."
                );
            }

            if (matches(interests, "nature", "relaxation", "beaches", "beach")) {
                activities.add(
                        "Visit a public park or the Hussain Sagar area. "
                        + "Hyderabad is inland and does not have a natural seaside beach."
                );
            }

            if (matches(interests, "spiritual", "spiritual places")) {
                activities.add(
                        "Visit a temple or another place of worship."
                );
            }

            if (matches(interests, "adventure")) {
                activities.add(
                        "Choose an affordable public recreation option "
                        + "or skip paid activities if they exceed the allowance."
                );
            }

            if (matches(interests, "nightlife")) {
                activities.add(
                        "Explore a public evening area and avoid paid "
                        + "venues that exceed your spending limit."
                );
            }

        } else {
            activities.add(
                    "Explore a public attraction in " + destination
                    + " after checking its location, access and entry fee."
            );

            if (matches(interests, "food")) {
                activities.add(
                        "Look for affordable local food in " + destination + "."
                );
            }

            if (matches(interests, "history", "heritage")) {
                activities.add(
                        "Explore a heritage area if entry and transport "
                        + "costs fit your available allowance."
                );
            }

            if (matches(interests, "nature", "relaxation")) {
                activities.add(
                        "Consider a public park, garden or scenic location."
                );
            }

            if (matches(interests, "adventure")) {
                activities.add(
                        "Check activity prices first. Prefer free alternatives "
                        + "when paid options exceed the budget."
                );
            }

            if (matches(interests, "spiritual", "spiritual places")) {
                activities.add(
                        "Explore a place of worship that matches your interests."
                );
            }

            if (matches(interests, "beaches", "beach")) {
                activities.add(
                        "Check whether the destination has accessible beaches "
                        + "before planning a visit."
                );
            }

            if (matches(interests, "nightlife")) {
                activities.add(
                        "Explore a suitable public evening area or market."
                );
            }

            if (matches(interests, "boat", "boating")) {
                activities.add(
                        "Check boating prices first and choose a free "
                        + "waterfront visit if necessary."
                );
            }
        }

        if (activities.isEmpty()) {
            activities.add(
                    "Explore public areas and local attractions with "
                    + "low or no entry fees."
            );
            activities.add(
                    "Try affordable local food and explore nearby places."
            );
            activities.add(
                    "Keep time for relaxation and prepare for departure."
            );
        }

        List<String> itinerary = new ArrayList<>();

        for (int day = 1; day <= days; day++) {
            LocalDate date = start.plusDays(day - 1);
            StringBuilder plan = new StringBuilder();

            plan.append("Day ")
                    .append(day)
                    .append(" (")
                    .append(date)
                    .append("): ");

            if (day == 1) {
                plan.append("Arrival and check-in. ");
            }

            if (day == days && days > 1) {
                plan.append("Keep time for checkout and departure. ");
            }

            int index = (day - 1) % activities.size();
            plan.append(activities.get(index));

            itinerary.add(plan.toString());
        }

        return itinerary;
    }

    // --------------------------------------------------
    // INTEREST MATCHING
    // --------------------------------------------------

    private boolean matches(List<String> interests, String... options) {
        if (interests == null || interests.isEmpty()) {
            return false;
        }

        for (String interest : interests) {
            if (interest == null || interest.isBlank()) {
                continue;
            }

            String value = normalize(interest);

            for (String option : options) {
                if (value.equals(normalize(option))) {
                    return true;
                }
            }
        }

        return false;
    }

    // --------------------------------------------------
    // TRANSPORT ADVICE
    // --------------------------------------------------

    private String buildTransportAdvice(String arrival, String local) {
        StringBuilder advice = new StringBuilder();

        if (arrival.equals("own_vehicle")
                || arrival.equals("car")
                || arrival.equals("bike")) {
            advice.append(
                    "Check fuel, tolls, parking and road conditions. "
            );
        } else if (arrival.equals("bus")) {
            advice.append(
                    "Compare bus fares and arrival-point transfer costs. "
            );
        } else if (arrival.equals("train")) {
            advice.append(
                    "Compare train fares and station transfer costs. "
            );
        } else if (arrival.equals("flight")) {
            advice.append(
                    "Check flight fares and airport transfer costs. "
            );
        } else {
            advice.append(
                    "Check the arrival transport and transfer costs. "
            );
        }

        if (local.equals("public_transport")) {
            advice.append(
                    "Prefer public transport where available and affordable."
            );
        } else if (local.equals("taxi") || local.equals("cab")) {
            advice.append(
                    "Compare taxi fares and use them only if they fit your limit."
            );
        } else if (local.equals("own_vehicle")) {
            advice.append(
                    "Include local fuel and parking costs in your allowance."
            );
        } else {
            advice.append(
                    "Compare local transport options against your allowance."
            );
        }

        return advice.toString();
    }

    // --------------------------------------------------
    // GOOGLE MAPS ROUTE LINK
    // --------------------------------------------------

    private String buildRouteUrl(
            String source,
            String destination,
            String arrival) {

        if (!(arrival.equals("own_vehicle")
                || arrival.equals("car")
                || arrival.equals("bike"))) {
            return "";
        }

        String origin = URLEncoder.encode(
                source, StandardCharsets.UTF_8
        );

        String target = URLEncoder.encode(
                destination, StandardCharsets.UTF_8
        );

        return "https://www.google.com/maps/dir/?api=1"
                + "&origin=" + origin
                + "&destination=" + target
                + "&travelmode=driving";
    }

    // --------------------------------------------------
    // MONEY ROUNDING
    // --------------------------------------------------

    private double roundMoney(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // --------------------------------------------------
    // NORMALIZE USER INPUT
    // --------------------------------------------------

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value.trim()
                .toLowerCase(Locale.ROOT)
                .replace("-", "_")
                .replace(" ", "_");
    }
}
