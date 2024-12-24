package onul.restapi.exercise.repository;

import io.lettuce.core.dynamic.annotation.Param;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.ExerciseRecord;
import onul.restapi.exercise.entity.ExerciseServiceNumber;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {

    Optional<ExerciseRecord> findByRecordDateAndExerciseServiceNumberAndSetNumberAndExercise(
            LocalDate recordDate,
            ExerciseServiceNumber exerciseServiceNumber,
            int setNumber,
            Exercise exercise
    );

    //삭제
    Optional<ExerciseRecord> findByMemberAndExerciseAndExerciseServiceNumberAndSetNumberAndRecordDate(
            Members member,
            Exercise exercise,
            ExerciseServiceNumber exerciseServiceNumber,
            int setNumber,
            LocalDate recordDate
    );

    @Query("SELECT DISTINCT er.recordDate FROM ExerciseRecord er " +
            "JOIN er.member m " +
            "JOIN er.exercise e " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE m.memberId = :memberId " +
            "AND e.id = :exerciseId " +
            "AND esn.id = :exerciseService")
    List<LocalDate> findDistinctRecordDatesByMemberIdAndExerciseIdAndServiceNumber(
            @Param("memberId") String memberId,
            @Param("exerciseId") Long exerciseId,
            @Param("exerciseService") Integer exerciseService);


    @Query("SELECT DISTINCT er.recordDate FROM ExerciseRecord er " +
            "JOIN er.member m " +
            "JOIN er.exercise e " +
            "JOIN er.exerciseServiceNumber esn " +
            "WHERE m.memberId = :memberId " +
            "AND e.id = :exerciseId " +
            "AND esn.id = :exerciseService " +
            "AND er.recordDate >= :threeMonthsAgo " +
            "ORDER BY er.recordDate DESC")
    List<LocalDate> findRecentRecordDates(
            @Param("memberId") String memberId,
            @Param("exerciseId") Long exerciseId,
            @Param("exerciseService") Integer exerciseService,
            @Param("threeMonthsAgo") LocalDate threeMonthsAgo);




}
