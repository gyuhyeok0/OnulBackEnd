package onul.restapi.management.repository;

import onul.restapi.management.entity.FoodEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodEntityRepository extends JpaRepository<FoodEntity, Long> {
    // 필요한 추가 메서드 정의 가능

    Optional<FoodEntity> findByRecipeId(String recipeId);

    Optional<FoodEntity> findByRecipeIdAndMember(String recipeId, Members member);

    List<FoodEntity> findByMember(Members member);

}
