package onul.restapi.management.repository;

import onul.restapi.management.entity.TotalFoodData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TotalFoodDataRepository extends JpaRepository<TotalFoodData, Long> {

    TotalFoodData findByMember_memberIdAndDate(String memberId, LocalDate date);

    // 필요한 쿼리 메서드를 추가할 수 있습니다.
}
