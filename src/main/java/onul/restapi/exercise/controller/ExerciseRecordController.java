package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.ExerciseDto;
import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.dto.ExerciseRecordSearchDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.service.ExerciseRecordService;
import onul.restapi.exercise.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/exercisesRecord")
public class ExerciseRecordController {

    private final ExerciseRecordService exerciseRecordService;

    public ExerciseRecordController(ExerciseRecordService exerciseRecordService) {
        this.exerciseRecordService = exerciseRecordService;
    }

    @PostMapping("/searchRecord")
    public ResponseEntity<List<ExerciseRecordDTO>> searchExerciseRecords(
            @RequestBody ExerciseRecordSearchDTO searchDTO) {

        // 서비스 호출로 데이터 조회
        List<ExerciseRecordDTO> records = exerciseRecordService.searchExerciseRecords(
                searchDTO.getExerciseId(),
                searchDTO.getMemberId(),
                searchDTO.getExerciseService(),
                searchDTO.getRecordDate()
        );

        // 데이터가 없으면 204 No Content 반환
        if (records.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // 데이터 반환 (Content-Type 명시적으로 설정)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                .body(records); // 응답 Body 설정
    }




}
