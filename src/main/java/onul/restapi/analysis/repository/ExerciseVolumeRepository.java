package onul.restapi.analysis.repository;

import io.lettuce.core.dynamic.annotation.Param;
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


}
