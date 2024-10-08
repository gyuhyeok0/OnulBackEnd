package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.MyExerciseDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.entity.MyExercise;
import onul.restapi.exercise.service.MyExerciseService;
import onul.restapi.member.controller.StateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myExercises")
public class MyExerciseController {

    private final MyExerciseService myExerciseService;

    public MyExerciseController(MyExerciseService myExerciseService) {
        this.myExerciseService = myExerciseService;
    }

    @GetMapping("/{memberId}/{muscleGroup}")
    public ResponseEntity<?> getMyExercises(
            @PathVariable String memberId,
            @PathVariable String muscleGroup) {
        try {
            // 서비스 호출하여 해당 회원과 근육 그룹에 대한 운동 목록 조회
            List<Exercise> exercises = myExerciseService.getMyExercises(memberId, muscleGroup);

            // 데이터가 없는 경우 "None" 응답을 반환
            if (exercises == null || exercises.isEmpty()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("None");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exercises);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: " + e.getMessage()));
        }
    }



    @PostMapping("/RegistMyExercise")
    public ResponseEntity<?> registerExercises(@RequestBody MyExerciseDTO myExerciseDTO) {

        System.out.println("컨트롤러"+ myExerciseDTO);
        try {

            // DTO에서 회원 ID와 운동 목록을 가져와서 등록 처리
            myExerciseService.registerMyExercises(myExerciseDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("SUCCESS"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: " + e.getMessage()));
        }
    }


    @DeleteMapping("/DeleteMyExercise/{exerciseId}")
    public ResponseEntity<?> deleteExercise(
            @PathVariable Long exerciseId,
            @RequestBody MyExerciseDTO myExerciseDTO) {
        try {
            // MyExerciseDTO에서 회원 ID와 근육 그룹 가져오기
            String memberId = myExerciseDTO.getMemberId();
            String muscleGroup = myExerciseDTO.getMuscleGroup();

            // 서비스 호출하여 운동 삭제 처리
            myExerciseService.deleteMyExercise(exerciseId, memberId, muscleGroup);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("FAILURE: " + e.getMessage()));
        }
    }

}
