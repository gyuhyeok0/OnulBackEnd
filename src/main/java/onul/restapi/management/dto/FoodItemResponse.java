package onul.restapi.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import onul.restapi.management.entity.FoodItemEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemResponse {

    private String foodName; // 음식 이름
    private String quantity; // 음식 양
    private String calories; // 칼로리
    private String protein; // 단백질
    private String carbs; // 탄수화물
    private String fat; // 지방

    // 정적 팩토리 메서드를 통해 FoodItemEntity를 FoodItemResponse로 변환
    public static FoodItemResponse fromEntity(FoodItemEntity foodItem) {
        return new FoodItemResponse(
                foodItem.getFoodName(),
                foodItem.getQuantity(),
                foodItem.getCalories(),
                foodItem.getProtein(),
                foodItem.getCarbs(),
                foodItem.getFat()
        );
    }
}
