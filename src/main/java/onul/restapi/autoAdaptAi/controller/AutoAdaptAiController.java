package onul.restapi.autoAdaptAi.controller;

import onul.restapi.analysis.dto.ExerciseVolumeResponse;
import onul.restapi.analysis.dto.MuscleFatigueDTO;
import onul.restapi.analysis.service.AnalysisService;
import onul.restapi.autoAdaptAi.dto.AiRecommendationRequestDTO;
import onul.restapi.autoAdaptAi.dto.AutoAdaptRequestDTO;
import onul.restapi.autoAdaptAi.dto.AutoAdaptSettingDTO;
import onul.restapi.autoAdaptAi.dto.AutoAdaptSettingRequstDTO;
import onul.restapi.autoAdaptAi.service.ExerciseSettingService;
import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.entity.AiExerciseRecordDTO;
import onul.restapi.exercise.service.ExerciseRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/autoAdapt")
public class AutoAdaptAiController {

    private final ExerciseSettingService exerciseSettingService;
    private final AnalysisService   analysisService;
    private final ExerciseRecordService exerciseRecordService;

    public AutoAdaptAiController(ExerciseSettingService exerciseSettingService, AnalysisService analysisService, ExerciseRecordService exerciseRecordService) {
        this.exerciseSettingService = exerciseSettingService;
        this.analysisService = analysisService;
        this.exerciseRecordService = exerciseRecordService;
    }

    @GetMapping(value = "/getAutoAdaptSetting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAutoAdaptSetting(@RequestParam("memberId") String memberId) {
        AutoAdaptSettingDTO response = exerciseSettingService.selectAutoAdaptSetting(memberId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/updateAutoAdaptSetting", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateAutoAdaptSetting(@RequestBody AutoAdaptSettingRequstDTO request) {

        System.out.println("durlghkrdls"+request);
        try {
            exerciseSettingService.updateAutoAdaptSetting(request);

            // ✅ 성공 시 "status": "success" 만 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status", "error"));
        }
    }


    // ✅ 새로운 AI 추천 요청 API 추가
    @PostMapping(value = "/aiRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requestAiRecommendation(@RequestBody AutoAdaptRequestDTO request) {

        LocalDate endDate = LocalDate.now().minusDays(1); // 어제 (오늘 제외)
        LocalDate startDate = endDate.minusDays(6); // 어제로부터 6일 전 (총 7일간 조회)

        if(request.isCheckDate()){

            System.out.println("여기서 날짜 확인후 있으면 종료 없으면 그대로 진행");

            return null;
        }

        System.out.println("db에서 정보 가지고옴");
        // 여기서 db 에 있는 정보를 가지고옴

        //1. 운동 세팅
        AutoAdaptSettingDTO defaltExerciseSetting = exerciseSettingService.selectAutoAdaptSetting(request.getMemberId());
        System.out.println(defaltExerciseSetting);

        //2. 유저가 운동을통해 db에 저장된 정보를 가지고 옴

        //2-1 최근 운동량 볼륨 변화 (1일전~8일전)
        ExerciseVolumeResponse recentVolumeByMuscleGroup = analysisService.getExerciseVolume(request.getMemberId(), startDate, endDate);

        //2-2 오늘 운동 피로도
        Map<String, List<MuscleFatigueDTO>> todayExerciseFatigue = analysisService.getMuscleFatigueByMemberAndToday(request.getMemberId());

        // 날짜별 운동 기록 가져오기
        Map<LocalDate, List<AiExerciseRecordDTO>> recentExercisesRecord = exerciseRecordService.getRecentExercisesGroupedByDate(request.getMemberId());


        // ✅ DTO 생성 (없는 데이터는 기본값 처리)
        AiRecommendationRequestDTO aiRequestDto = new AiRecommendationRequestDTO(
                defaltExerciseSetting,
                recentVolumeByMuscleGroup, // 없으면 null
                (todayExerciseFatigue != null) ? todayExerciseFatigue : Collections.emptyMap(), // 없으면 빈 맵
                (recentExercisesRecord != null) ? recentExercisesRecord : Collections.emptyMap() // 없으면 빈 맵
        );

        System.out.println(aiRequestDto);







        return null;
    }



}
