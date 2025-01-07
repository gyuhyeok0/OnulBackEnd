package onul.restapi.management.service;

import onul.restapi.management.dto.FoodDataRequest;
import onul.restapi.management.entity.FoodEntity;
import onul.restapi.management.entity.FoodItemEntity;
import onul.restapi.management.repository.FoodEntityRepository;
import onul.restapi.management.repository.FoodItemEntityRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FoodService {

    private final FoodEntityRepository foodEntityRepository;
    private final FoodItemEntityRepository foodItemEntityRepository;
    private final MemberRepository memberRepository;

    public FoodService(FoodEntityRepository foodEntityRepository, FoodItemEntityRepository foodItemEntityRepository, MemberRepository memberRepository) {
        this.foodEntityRepository = foodEntityRepository;
        this.foodItemEntityRepository = foodItemEntityRepository;
        this.memberRepository = memberRepository;
    }


    @Transactional
    public void saveFoodData(FoodDataRequest foodDataRequest) {
        // 1. Members 조회
        Members member = memberRepository.findById(foodDataRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + foodDataRequest.getMemberId()));

        // 2. FoodEntity 존재 여부 확인
        Optional<FoodEntity> existingFoodEntity = foodEntityRepository.findByRecipeId(foodDataRequest.getRecipeId());

        // 3. FoodEntity 생성 또는 업데이트
        FoodEntity foodEntity = existingFoodEntity
                .map(entity -> FoodEntity.builder()
                        .id(entity.getId()) // 기존 ID 유지
                        .member(member)
                        .recipeId(foodDataRequest.getRecipeId())
                        .recipeName(foodDataRequest.getRecipeName()) // 레시피 이름 업데이트
                        .build())
                .orElse(FoodEntity.builder()
                        .member(member)
                        .recipeId(foodDataRequest.getRecipeId())
                        .recipeName(foodDataRequest.getRecipeName())
                        .build());

        foodEntityRepository.save(foodEntity);

        // 4. 기존 FoodItemEntity 삭제
        foodItemEntityRepository.deleteByMemberAndFoodEntity(member, foodEntity);

        // 5. FoodItemEntity 생성 및 저장
        foodDataRequest.getFoodItems().forEach(foodItem -> {
            FoodItemEntity newFoodItem = FoodItemEntity.builder()
                    .foodName(foodItem.getFoodName())
                    .quantity(foodItem.getQuantity())
                    .calories(foodItem.getCalories())
                    .protein(foodItem.getProtein())
                    .carbs(foodItem.getCarbs())
                    .fat(foodItem.getFat())
                    .foodEntity(foodEntity)
                    .member(member)
                    .build();

            foodItemEntityRepository.save(newFoodItem);
        });
    }



}
