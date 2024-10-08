package onul.restapi.exercise.service;

import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


    // 목록 좋아요
    public boolean toggleLike(Long exerciseId, Boolean isLiked) {
        // 운동 ID로 운동 정보 조회
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);

        if (exerciseOpt.isPresent()) {
            Exercise exercise = exerciseOpt.get();

            // Builder를 사용하여 새로운 Exercise 객체 생성
            Exercise updatedExercise = Exercise.builder()
                    .id(exercise.getId())
                    .exerciseName(exercise.getExerciseName())
                    .mainMuscleGroup(exercise.getMainMuscleGroup())
                    .detailMuscleGroup(exercise.getDetailMuscleGroup())
                    .popularityGroup(exercise.getPopularityGroup())
                    .isLiked(isLiked) // 새로운 좋아요 상태 설정
                    .build();

            exerciseRepository.save(updatedExercise);
            return true; // 상태 변경 성공
        } else {
            return false; // 운동이 존재하지 않음
        }
    }

}
