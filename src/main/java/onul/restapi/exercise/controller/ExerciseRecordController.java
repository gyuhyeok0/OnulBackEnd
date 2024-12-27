package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.dto.ExerciseRecordSearchDTO;
import onul.restapi.exercise.dto.ExerciseVolumeRequest;
import onul.restapi.exercise.dto.RecordDateRequest;
import onul.restapi.exercise.service.ExerciseRecordService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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


    @PostMapping("/selectPreviousRecordDate")
    public ResponseEntity<List<String>> getPreviousRecordDates(
            @RequestBody RecordDateRequest request
    ) {
        try {
            // Service에서 운동 기록 날짜 목록 가져오기
            List<LocalDate> recordDates = exerciseRecordService.getPreviousRecordDates(
                    request.getMemberId(),
                    request.getExerciseId(),
                    request.getExerciseService()
            );

            if (recordDates.isEmpty()) {
                return ResponseEntity.noContent().build(); // 날짜가 없는 경우 204 No Content 반환
            }

            // LocalDate 리스트를 String 리스트로 변환
            List<String> recordDateStrings = recordDates.stream()
                    .map(LocalDate::toString)
                    .toList();

            // JSON 형식으로 응답 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON) // Content-Type 설정
                    .body(recordDateStrings);

        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/searchVolume")
    public ResponseEntity<?> searchVolume(@RequestBody ExerciseVolumeRequest request) {
        try {
            // 요청 아이디 로그 출력
            System.out.println("요청 아이디: " + request.getExerciseIds());

            // 요청 데이터를 서비스 레이어로 전달
            List<ExerciseRecordDTO> records = exerciseRecordService.searchVolumeRecords(request);

            // 결과 로그 출력
            System.out.println("확인 필요: " + records);

            // 결과가 비어있을 경우 204 No Content 반환
            if (records.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // JSON 형식으로 응답 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON) // JSON 형식 설정
                    .body(records);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }



//    @PostMapping("/selectMostPreviousRecordDate")
//    public ResponseEntity<String> selectMostPreviousRecordDate(
//            @RequestBody RecordDateRequest request
//    ) {
//        try {
//            // Service에서 가장 오래된 운동 기록 날짜 가져오기
//            LocalDate recordDates = exerciseRecordService.getMostPreviousRecordDates(
//                    request.getMemberId(),
//                    request.getExerciseId(),
//                    request.getExerciseService()
//            );
//
//            System.out.println("recordDates: " + recordDates);
//
//            if (recordDates == null) {
//                return ResponseEntity.noContent().build(); // 날짜가 없는 경우 204 No Content 반환
//            }
//
//            // 문자열로 변환하여 TEXT 형식으로 응답 반환
//            return ResponseEntity.ok()
//                    .contentType(MediaType.TEXT_PLAIN) // Content-Type 설정
//                    .body(recordDates.toString());
//
//        } catch (Exception e) {
//            // 예외 발생 시 500 Internal Server Error 반환
//            return ResponseEntity.status(500).build();
//        }
//    }
//

}
