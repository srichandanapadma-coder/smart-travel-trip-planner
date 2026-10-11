package com.smarttravel.smarttravel.controller;

import com.smarttravel.smarttravel.dto.RouteResponse;
import com.smarttravel.smarttravel.service.RouteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> getRoute(
            @RequestParam String source,
            @RequestParam String destination) {

        try {

            RouteResponse response =
                    routeService.getRoute(source, destination);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();


            return ResponseEntity .badRequest().build();
        }
    }
}