package onul.restapi.management.controller;

import onul.restapi.management.dto.FoodDataRequest;

import onul.restapi.management.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
public class FoodController {

    private final FoodService foodService;
//
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/saveFoodData")
    public ResponseEntity<?> saveFoodData(@RequestBody FoodDataRequest foodDataRequest) {
        try {
            foodService.saveFoodData(foodDataRequest);

            return ResponseEntity.ok("Food data saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save food data: " + e.getMessage());
        }
    }
}
