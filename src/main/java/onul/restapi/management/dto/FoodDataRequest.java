package onul.restapi.management.dto;

import java.util.List;

public class FoodDataRequest {

    private String memberId;
    private String recipeId; // 레시피 ID
    private String recipeName;
    private List<FoodItem> foodItems;

    // Getters and Setters
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @Override
    public String toString() {
        return "FoodDataRequest{" +
                "memberId='" + memberId + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", foodItems=" + foodItems +
                '}';
    }

    public static class FoodItem {
        private String foodName;
        private String quantity;
        private String calories;
        private String protein;
        private String carbs;
        private String fat;

        // Getters and Setters
        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getProtein() {
            return protein;
        }

        public void setProtein(String protein) {
            this.protein = protein;
        }

        public String getCarbs() {
            return carbs;
        }

        public void setCarbs(String carbs) {
            this.carbs = carbs;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        @Override
        public String toString() {
            return "FoodItem{" +
                    "foodName='" + foodName + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", calories='" + calories + '\'' +
                    ", protein='" + protein + '\'' +
                    ", carbs='" + carbs + '\'' +
                    ", fat='" + fat + '\'' +
                    '}';
        }
    }
}
