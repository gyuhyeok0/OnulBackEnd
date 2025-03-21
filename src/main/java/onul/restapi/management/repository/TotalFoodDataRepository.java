package onul.restapi.management.repository;

import onul.restapi.management.entity.TotalFoodData;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TotalFoodDataRepository extends JpaRepository<TotalFoodData, Long> {

    TotalFoodData findByMember_memberIdAndDate(String memberId, LocalDate date);

    boolean existsByMemberAndDate(Members member, LocalDate date);

    List<TotalFoodData> findAllByMember_memberIdAndDate(String memberId, LocalDate date);

    TotalFoodData findByMember_memberIdAndDateAndMealType(String memberId, LocalDate date, String mealType);

    List<TotalFoodData> findByMemberAndDateBetween(Members member, LocalDate monthStart, LocalDate monthEnd);

    void deleteByMember(Members member);

    @Query("SELECT f.date FROM TotalFoodData f WHERE f.member = :member AND f.date BETWEEN :startDate AND :endDate")
    List<LocalDate> findFoodDates(@Param("member") Members member, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // 필요한 쿼리 메서드를 추가할 수 있습니다.
}
