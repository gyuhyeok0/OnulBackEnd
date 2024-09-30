package onul.restapi.exercise.controller;

import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // 운동 목록 조회 (GET /exercises/selectList)
    @GetMapping(value = "/selectList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Exercise>> getExerciseList() {
        List<Exercise> exercises = exerciseService.findAllExercises();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exercises);
    }
}
