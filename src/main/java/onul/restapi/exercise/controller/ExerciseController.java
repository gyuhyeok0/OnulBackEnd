package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.ExerciseDto;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // 운동등록
    // 운동 목록 조회 (GET /exercises/selectList)
    @GetMapping(value = "/selectList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Exercise>> getExerciseList() {
        List<Exercise> exercises = exerciseService.findAllExercises();


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exercises);
    }

    // 운동등록
    // 좋아요 상태
    @PostMapping("/{exerciseId}/isLiked")
    public ResponseEntity<String> toggleLike(@PathVariable Long exerciseId, @RequestBody ExerciseDto request) {
        Boolean isLiked = request.getLiked();

        // 서비스 호출하여 좋아요 상태 업데이트
        boolean updatedStatus = exerciseService.toggleLike(exerciseId, isLiked);

        if (updatedStatus) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON) // JSON 형식으로 설정
                    .body("{\"message\": \"좋아요 상태가 변경되었습니다.\"}"); // JSON 형식으로 응답 본문
        } else {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON) // JSON 형식으로 설정
                    .body("{\"message\": \"좋아요 상태 변경 실패.\"}"); // JSON 형식으로 응답 본문
        }
    }




}
