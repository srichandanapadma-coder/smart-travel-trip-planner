
package com.smarttravel.smarttravel.planner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/plan")
    public ResponseEntity<?> planTrip(
            @RequestBody PlannerRequest request) {
        try {
            return ResponseEntity.ok(
                    plannerService.planTrip(request)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid trip details.")
            );
        }
    }
}

