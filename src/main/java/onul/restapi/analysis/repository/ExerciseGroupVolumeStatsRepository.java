package onul.restapi.analysis.repository;

import onul.restapi.analysis.entity.ExerciseGroupVolumeStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseGroupVolumeStatsRepository extends JpaRepository<ExerciseGroupVolumeStatsEntity, Long> {

    // 특정 회원의 주간 또는 월간 데이터 조회
    List<ExerciseGroupVolumeStatsEntity> findByMember_MemberIdAndPeriodTypeAndStartDateBetween(
            String memberId,
            String periodType,
            LocalDate startDate,
            LocalDate endDate
    );


    ExerciseGroupVolumeStatsEntity findByMember_MemberIdAndPeriodTypeAndMainMuscleGroupAndStartDate(String memberId, String weekly, String muscleGroup, LocalDate startOfWeek);

}
