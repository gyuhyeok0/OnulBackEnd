package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.service.ExerciseRecordService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/submitExercises")
public class SubmitExerciseController {

    private final ExerciseRecordService exerciseRecordService;

    public SubmitExerciseController(ExerciseRecordService exerciseRecordService) {
        this.exerciseRecordService = exerciseRecordService;
    }

    // 운동 기록 저장
    @PostMapping("/regist")
    public ResponseEntity<?> registExerciseRecord(
            @RequestBody ExerciseRecordDTO exerciseRecordDTO,
            @RequestParam("date") LocalDate date) {

        System.out.println("클라이언트 시간 "+date);

        exerciseRecordDTO.setRecordDate(date);

        try {
            // ExerciseRecordService로 전달하여 저장 처리
            exerciseRecordService.saveExerciseRecord(exerciseRecordDTO);

            // JSON 형식으로 응답 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 본문을 JSON 형식으로 설정
                    .body("{\"message\":\"운동 기록이 성공적으로 저장되었습니다.\"}");
        } catch (Exception e) {
            // 예외 발생 시 JSON 형식으로 에러 메시지 반환

            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\":\"운동 기록 저장 중 오류가 발생했습니다.\"}");
        }
    }


    // 운동 기록 삭제
    @PostMapping("/delete")
    public ResponseEntity<?> deleteExerciseRecord(
            @RequestBody ExerciseRecordDTO exerciseRecordDTO,
            @RequestParam("date") LocalDate date){

        System.out.println(date);
        exerciseRecordDTO.setRecordDate(date);

        try {
            // ExerciseRecordService로 전달하여 삭제 처리
            exerciseRecordService.deleteExerciseRecord(exerciseRecordDTO);

            // JSON 형식으로 응답 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 본문을 JSON 형식으로 설정
                    .body("{\"message\":\"운동 기록이 성공적으로 삭제되었습니다.\"}");
        } catch (Exception e) {
            // 예외 발생 시 JSON 형식으로 에러 메시지 반환
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\":\"운동 기록 삭제 중 오류가 발생했습니다.\"}");
        }
    }






}
