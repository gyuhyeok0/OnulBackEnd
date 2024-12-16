package onul.restapi.exercise.repository;

import io.lettuce.core.dynamic.annotation.Param;
import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.ExerciseRecord;
import onul.restapi.exercise.entity.MyExercise;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> findByExerciseName(String exerciseName);

    @Query("SELECT r FROM ExerciseRecord r " +
            "WHERE r.exercise.id = :exerciseId " +
            "AND r.member.memberId = :memberId " +
            "AND r.exerciseServiceNumber.id = :exerciseService " +
            "AND r.recordDate = :recordDate")
    List<ExerciseRecord> findRecordsByConditions(
            @Param("exerciseId") Long exerciseId,
            @Param("memberId") String memberId,
            @Param("exerciseService") int exerciseService,
            @Param("recordDate") LocalDate recordDate
    );


}
