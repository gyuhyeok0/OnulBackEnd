package onul.restapi.exercise.repository;

import onul.restapi.exercise.entity.ExerciseServiceNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseServiceRepository extends JpaRepository<ExerciseServiceNumber, Long> {
}
