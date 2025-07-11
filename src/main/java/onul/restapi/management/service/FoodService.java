package onul.restapi.management.service;

import onul.restapi.management.dto.*;
import onul.restapi.management.entity.FoodEntity;
import onul.restapi.management.entity.FoodItemEntity;
import onul.restapi.management.entity.TotalFoodData;
import onul.restapi.management.repository.FoodEntityRepository;
import onul.restapi.management.repository.FoodItemEntityRepository;
import onul.restapi.management.repository.TotalFoodDataRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import onul.restapi.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodEntityRepository foodEntityRepository;
    private final FoodItemEntityRepository foodItemEntityRepository;
    private final TotalFoodDataRepository totalFoodDataRepository;
    private final MemberService memberService;

    public FoodService(FoodEntityRepository foodEntityRepository, FoodItemEntityRepository foodItemEntityRepository, TotalFoodDataRepository totalFoodDataRepository, MemberService memberService) {
        this.foodEntityRepository = foodEntityRepository;
        this.foodItemEntityRepository = foodItemEntityRepository;
        this.totalFoodDataRepository = totalFoodDataRepository;
        this.memberService = memberService;
    }

    @Transactional
    public void saveFoodData(FoodDataRequest foodDataRequest) {
        // 1. Members 조회
        Members member = memberService.getMemberById(foodDataRequest.getMemberId());

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




    @Transactional
    public void deleteFoodData(String memberId, String recipeId) {
        // 1. 회원 조회
        Members member = memberService.getMemberById(memberId);

        // 2. FoodEntity 조회
        Optional<FoodEntity> foodEntityOptional = foodEntityRepository.findByRecipeIdAndMember(recipeId, member);

        // 3. 데이터가 있으면 삭제
        if (foodEntityOptional.isPresent()) {
            FoodEntity foodEntity = foodEntityOptional.get();

            // food_items 데이터 삭제
            foodItemEntityRepository.deleteByMemberAndFoodEntity(member, foodEntity);

            // food_recipes 데이터 삭제
            foodEntityRepository.delete(foodEntity);
        }
        // 데이터가 없으면 아무 작업도 하지 않음
    }




    @Transactional(readOnly = true)
    public List<FoodRecipeResponse> getAllFoodData(String memberId) {
        try {
            // 회원 조회
            Members member = memberService.getMemberById(memberId);

            // FoodEntity 조회
            List<FoodEntity> foodEntities = foodEntityRepository.findByMember(member);

            // DTO 변환
            return foodEntities.stream()
                    .map(foodEntity -> {
                        List<FoodItemEntity> foodItems = foodItemEntityRepository.findByFoodEntity(foodEntity);


                        return new FoodRecipeResponse(
                                foodEntity.getRecipeId(),
                                foodEntity.getRecipeName(),
                                foodItems.stream()
                                        .map(FoodItemResponse::fromEntity) // FoodItemEntity -> FoodItemResponse 변환
                                        .collect(Collectors.toList())
                        );
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error occurred in getAllFoodData: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스 출력
            throw e; // 예외 재발생
        }
    }



    @Transactional
    public SavedFoodDataResponse saveTotalFoodData(SaveTotalFoodDataRequest request) {
        // 요청에서 전달받은 회원 ID를 사용하여 회원 정보를 조회합니다.
        Members member = memberService.getMemberById(request.getMemberId());

        // 요청에서 전달받은 날짜와 회원 ID, mealType에 해당하는 TotalFoodData를 조회합니다.
        TotalFoodData existingFoodData = totalFoodDataRepository.findByMember_memberIdAndDateAndMealType(
                member.getMemberId(), request.getDate(), request.getMealType());

        if (existingFoodData != null) {
            // 데이터가 이미 존재하면 Builder를 사용하여 업데이트합니다.
            TotalFoodData updatedFoodData = existingFoodData.toBuilder()
                    .mealType(request.getMealType())  // 식사 종류 업데이트
                    .totalNutrition(request.getTotalNutrition())  // 영양 정보 업데이트
                    .recipeNames(request.getRecipeNames())  // 레시피 이름 목록 업데이트
                    .build();

            // 업데이트된 데이터를 저장합니다.
            totalFoodDataRepository.save(updatedFoodData);

            // 업데이트된 데이터를 SavedFoodDataResponse 객체로 반환
            return new SavedFoodDataResponse(
                    updatedFoodData.getMember().getMemberId(),
                    updatedFoodData.getMealType(),
                    updatedFoodData.getDate(),
                    updatedFoodData.getTotalNutrition(),
                    updatedFoodData.getRecipeNames()
            );
        } else {
            // 데이터가 없으면 새로운 데이터를 생성합니다.
            TotalFoodData newFoodData = TotalFoodData.builder()
                    .member(member)
                    .mealType(request.getMealType())  // 식사 종류 (예: Breakfast, Lunch, Dinner)
                    .date(request.getDate())  // 날짜 (yyyy-MM-dd 형식)
                    .totalNutrition(request.getTotalNutrition())  // 영양 정보 (Map)
                    .recipeNames(request.getRecipeNames())  // 레시피 이름 목록
                    .build();

            // 새 데이터를 데이터베이스에 저장합니다.
            TotalFoodData savedFoodData = totalFoodDataRepository.save(newFoodData);

            // 저장된 데이터를 SavedFoodDataResponse 객체로 반환
            return new SavedFoodDataResponse(
                    savedFoodData.getMember().getMemberId(),
                    savedFoodData.getMealType(),
                    savedFoodData.getDate(),
                    savedFoodData.getTotalNutrition(),
                    savedFoodData.getRecipeNames()
            );
        }
    }



    @Transactional(readOnly = true)
    public List<SavedFoodDataResponse> getFoodRecordsForDate(String memberId, LocalDate date) {
        // 회원 조회

        // TotalFoodData 조회 (날짜와 회원 ID 기준으로 모든 데이터 조회)
        List<TotalFoodData> totalFoodDataList = totalFoodDataRepository.findAllByMember_memberIdAndDate(memberId, date);

        if (totalFoodDataList.isEmpty()) {
            throw new IllegalArgumentException("No food records found for the given member and date.");
        }


        // TotalFoodData 리스트를 SavedFoodDataResponse 리스트로 변환
        return totalFoodDataList.stream()
                .map(totalFoodData -> new SavedFoodDataResponse(
                        totalFoodData.getMember().getMemberId(),
                        totalFoodData.getMealType(),
                        totalFoodData.getDate(),
                        totalFoodData.getTotalNutrition(),
                        totalFoodData.getRecipeNames()
                ))
                .collect(Collectors.toList());
    }
}
