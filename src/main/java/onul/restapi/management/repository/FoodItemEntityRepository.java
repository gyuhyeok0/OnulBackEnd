package onul.restapi.management.repository;

import io.lettuce.core.dynamic.annotation.Param;
import onul.restapi.management.entity.FoodEntity;
import onul.restapi.management.entity.FoodItemEntity;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FoodItemEntityRepository extends JpaRepository<FoodItemEntity, Long> {


    void deleteByMemberAndFoodEntity(Members member, FoodEntity foodEntity);
}
