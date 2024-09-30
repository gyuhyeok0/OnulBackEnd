package onul.restapi.exercise.service;

import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    // 모든 운동 목록을 조회하는 메서드
    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }
}
