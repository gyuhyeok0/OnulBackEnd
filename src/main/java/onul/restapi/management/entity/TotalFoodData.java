package onul.restapi.management.entity;

import jakarta.persistence.*;
import lombok.*;
import onul.restapi.member.entity.Members;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "total_food", indexes = {
        @Index(name = "idx_member_date", columnList = "member_id, date"),
        @Index(name = "idx_member_date_meal", columnList = "member_id, date, mealType")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TotalFoodData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private Members member; // 회원 정보

    @Column(nullable = false)
    private String mealType; // 식사 종류 (예: Breakfast, Lunch, Dinner)

    @Column(nullable = false)
    private LocalDate date; // 날짜

    @ElementCollection
    @CollectionTable(name = "total_nutrition", joinColumns = @JoinColumn(name = "food_data_id"))
    @MapKeyColumn(name = "nutrition_type")
    @Column(name = "value")
    private Map<String, Double> totalNutrition; // 영양 정보 (예: grams, kcal, carbs, protein, fat)

    @ElementCollection
    @CollectionTable(name = "recipe_names", joinColumns = @JoinColumn(name = "food_data_id"))
    @Column(name = "recipe_name")
    private List<String> recipeNames; // 레시피 이름 목록


    // 데이터 저장 후 반환하는 생성자
    public TotalFoodData(Members member, String mealType, LocalDate date, Map<String, Double> totalNutrition, List<String> recipeNames) {
        this.member = member;
        this.mealType = mealType;
        this.date = date;
        this.totalNutrition = totalNutrition;
        this.recipeNames = recipeNames;
    }

    // toBuilder 메서드 구현
    public TotalFoodDataBuilder toBuilder() {
        return TotalFoodData.builder()
                .id(this.id)  // 기존 값으로 초기화
                .member(this.member)
                .mealType(this.mealType)
                .date(this.date)
                .totalNutrition(this.totalNutrition)
                .recipeNames(this.recipeNames); // recipeNames 추가
    }
}
