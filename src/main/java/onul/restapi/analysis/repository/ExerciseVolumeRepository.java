package onul.restapi.analysis.repository;

import io.lettuce.core.dynamic.annotation.Param;
import onul.restapi.analysis.entity.ExerciseGroupVolumeStatsEntity;
import onul.restapi.analysis.entity.ExerciseVolumeStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseVolumeRepository extends JpaRepository<ExerciseVolumeStatsEntity, Long> {


    // 특정 회원의 가장 최근 기록 날짜를 조회
    @Query("SELECT MAX(e.recordDate) FROM ExerciseVolumeStatsEntity e WHERE e.member.memberId = :memberId")
    Optional<LocalDate> findLatestRecordDateByMemberId(@Param("memberId") String memberId);

    @Query("SELECT e FROM ExerciseVolumeStatsEntity e WHERE e.member.memberId = :memberId AND e.recordDate = :date AND e.exercise.id = :exerciseId")
    Optional<ExerciseVolumeStatsEntity> findByMemberIdAndRecordDateAndExerciseId(@Param("memberId") String memberId, @Param("date") LocalDate date, @Param("exerciseId") Long exerciseId);

    List<ExerciseVolumeStatsEntity> findByRecordDateBetween(LocalDate startOfWeek, LocalDate endOfWeek);


    List<ExerciseVolumeStatsEntity> findByMember_MemberIdAndRecordDateBetween(String memberId, LocalDate startDate, LocalDate endDate);

    Optional<Object> findTopByMember_MemberIdAndDetailMuscleGroupAndRecordDateBeforeOrderByRecordDateDesc(String memberId, String detailMuscle, LocalDate startDate);


    @Query("SELECT e FROM ExerciseGroupVolumeStatsEntity e WHERE e.member.memberId = :memberId AND e.periodType = :periodType AND e.startDate BETWEEN :startDate AND :endDate")
    List<ExerciseGroupVolumeStatsEntity> findByMember_MemberIdAndPeriodTypeAndStartDateBetween(
            @Param("memberId") String memberId,
            @Param("periodType") String periodType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
