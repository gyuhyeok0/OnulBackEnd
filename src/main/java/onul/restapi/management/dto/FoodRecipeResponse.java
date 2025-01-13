package onul.restapi.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodRecipeResponse {
    private String recipeId; // FoodEntity의 recipeId
    private String recipeName; // FoodEntity의 recipeName
    private List<FoodItemResponse> foodItems; // FoodItemEntity 데이터를 담는 리스트
}
