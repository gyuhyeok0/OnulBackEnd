package onul.restapi.management.repository;

import onul.restapi.analysis.entity.WeightAndDietStatistics;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WeightAndDietStatisticsRepository extends JpaRepository<WeightAndDietStatistics, Long> {

    // 특정 회원과 날짜에 해당하는 통계 가져오기
    WeightAndDietStatistics findByMemberAndDate(Members member, LocalDate date);

}
