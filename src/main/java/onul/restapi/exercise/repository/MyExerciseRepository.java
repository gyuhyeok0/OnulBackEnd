package onul.restapi.exercise.repository;

import onul.restapi.exercise.entity.MyExercise;
import onul.restapi.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyExerciseRepository extends JpaRepository<MyExercise, Long> {


    MyExercise findByMemberAndMuscleGroup(Members member, String muscleGroup);

}
