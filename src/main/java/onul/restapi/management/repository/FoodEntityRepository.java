package onul.restapi.management.repository;

import onul.restapi.management.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodEntityRepository extends JpaRepository<FoodEntity, Long> {
    // 필요한 추가 메서드 정의 가능

    Optional<FoodEntity> findByRecipeId(String recipeId);
}
