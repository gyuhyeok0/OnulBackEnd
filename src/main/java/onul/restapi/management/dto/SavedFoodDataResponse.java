package onul.restapi.management.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SavedFoodDataResponse {

    private String memberId;
    private String mealType;
    private LocalDate date;
    private Map<String, Double> totalNutrition;
    private List<String> recipeNames; // 레시피 이름 목록 추가


    // 데이터 저장 후 반환하는 생성자
    public SavedFoodDataResponse(String memberId, String mealType, LocalDate date, Map<String, Double> totalNutrition, List<String> recipeNames) {
        this.memberId = memberId;
        this.mealType = mealType;
        this.date = date;
        this.totalNutrition = totalNutrition;
        this.recipeNames = recipeNames;
    }

    // Getters and Setters
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, Double> getTotalNutrition() {
        return totalNutrition;
    }

    public void setTotalNutrition(Map<String, Double> totalNutrition) {
        this.totalNutrition = totalNutrition;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public void setRecipeNames(List<String> recipeNames) {
        this.recipeNames = recipeNames;
    }
}
