package onul.restapi.management.controller;

import onul.restapi.management.dto.FoodDataRequest;

import onul.restapi.management.dto.FoodRecipeResponse;
import onul.restapi.management.dto.SaveTotalFoodDataRequest;
import onul.restapi.management.dto.SavedFoodDataResponse;
import onul.restapi.management.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/deleteFoodData")
    public ResponseEntity<?> deleteFoodData(@RequestParam String memberId, @RequestParam String recipeId) {
        try {

            System.out.println(memberId);
            System.out.println(recipeId);
            foodService.deleteFoodData(memberId, recipeId);
            return ResponseEntity.ok("Food data deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete food data: " + e.getMessage());
        }
    }



    @GetMapping(value = "/getAllFoodData", produces = "application/json")
    public ResponseEntity<?> getAllFoodData(@RequestParam String memberId) {
        try {
            List<FoodRecipeResponse> foodData = foodService.getAllFoodData(memberId);

            if (foodData == null || foodData.isEmpty()) {
                // 데이터가 없으면 404 상태 코드를 반환
                return ResponseEntity.status(404).body("데이터가 존재하지 않습니다.");
            }

            System.out.println(foodData);
            return ResponseEntity.ok(foodData);

        } catch (Exception e) {
            // 예상하지 못한 예외에 대해 500 상태 코드를 반환
            return ResponseEntity.status(500).body("서버 내부 오류가 발생했습니다.");
        }
    }


    @PostMapping(value = "/saveTotalFoodData", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SavedFoodDataResponse> saveTotalFoodData(@RequestBody SaveTotalFoodDataRequest request) {
        try {
            // DB에 데이터 저장
            SavedFoodDataResponse savedData = foodService.saveTotalFoodData(request);

            System.out.println(savedData);
            // 저장된 데이터를 클라이언트로 반환
            return ResponseEntity.ok(savedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 실패 시 null 반환
        }
    }




}
