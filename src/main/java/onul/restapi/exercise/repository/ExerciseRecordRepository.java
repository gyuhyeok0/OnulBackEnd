package onul.restapi.exercise.repository;

import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.ExerciseRecord;
import onul.restapi.exercise.entity.ExerciseServiceNumber;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
}
