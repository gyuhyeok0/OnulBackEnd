package onul.restapi.autoAdaptAi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import onul.restapi.analysis.dto.MuscleFatigueDTO;
import onul.restapi.analysis.service.AnalysisService;
import onul.restapi.autoAdaptAi.dto.*;
import onul.restapi.autoAdaptAi.service.AiRecommendationService;
import onul.restapi.autoAdaptAi.service.AutoAdaptService;
import onul.restapi.autoAdaptAi.service.ExerciseSettingService;
import onul.restapi.autoAdaptAi.service.RequestLimitService;
import onul.restapi.exercise.entity.AiExerciseRecordDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.service.ExerciseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/autoAdapt")
public class AutoAdaptAiController {


    @Autowired
    @Qualifier("asyncExecutor")
    private Executor asyncExecutor;

    private final ExerciseSettingService exerciseSettingService;
    private final AnalysisService   analysisService;
    private final ExerciseRecordService exerciseRecordService;
    private final AutoAdaptService autoAdaptService;
    private final RequestLimitService requestLimitService;
    private final AiRecommendationService aiRecommendationService;

    public AutoAdaptAiController(ExerciseSettingService exerciseSettingService, AnalysisService analysisService, ExerciseRecordService exerciseRecordService, AutoAdaptService autoAdaptService, RequestLimitService requestLimitService, AiRecommendationService aiRecommendationService) {
        this.exerciseSettingService = exerciseSettingService;
        this.analysisService = analysisService;
        this.exerciseRecordService = exerciseRecordService;
        this.autoAdaptService = autoAdaptService;
        this.requestLimitService = requestLimitService;
        this.aiRecommendationService = aiRecommendationService;
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

    @PostMapping(value = "/changePriorityPartsSetting", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePriorityPartsSetting(@RequestBody PriorityPartsRequestDTO request) {

        try {

            exerciseSettingService.changePriority(request);

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
    public CompletableFuture<ResponseEntity<?>> requestAiRecommendation(
            @RequestBody AutoAdaptRequestDTO request,
            @RequestParam("date") LocalDate date,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) throws JsonProcessingException {

        // true: 생성할때 "자동" 으로만 넣어야해

        // true : 날짜 확인후 날짜가 있으면 그대로 종류, 없으면 새로 생성
        // false : 날짜 확인 안해도 됨

//      1분 동안 최대 2번만 요청 허용 (3번째 요청부터 차단)
        if (!requestLimitService.isRequestAllowed(authHeader)) {
            System.out.println("⚠️ Too many requests from user. Request blocked. (UserToken: " + authHeader + ")");
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Too many requests", "message", "You can only request twice in 8 minutes.")));
        }


        // 조건문 밖에서 선언
        AutoAdaptSettingDTO defaltExerciseSetting;


        if(request.isCheckDate()){


            // ✅ 오늘 날짜에 데이터가 있는지 확인
            boolean exists = autoAdaptService.existsAutoAdaptForToday(request.getMemberId(),date);

            if (exists) {
                return null;  // 데이터가 있으면 종료
            }

            // 없을때 자동으로
            defaltExerciseSetting = exerciseSettingService.selectAutoAdaptSetting(request.getMemberId());
            // "자동" 하나만 포함된 새 리스트를 생성
            List<String> newPriorityParts = Collections.singletonList("자동");
            // 기존 값을 무시하고 새 리스트로 설정
            defaltExerciseSetting.setPriorityParts(newPriorityParts);

        } else {

            defaltExerciseSetting = exerciseSettingService.selectAutoAdaptSetting(request.getMemberId());

        }

        // ✅ 2️⃣ 오늘 운동 피로도 조회 후, 운동 기록 조회를 순차적으로 실행
        CompletableFuture<Map<String, List<MuscleFatigueDTO>>> todayExerciseFatigueFuture =
                CompletableFuture.supplyAsync(() ->
                        analysisService.getMuscleFatigueByMemberAndToday(request.getMemberId(), date), asyncExecutor
                ).exceptionally(ex -> {
                    System.err.println("⚠️ 운동 피로도 조회 실패: " + ex.getMessage());
                    return Collections.emptyMap();
                });

        // ✅ 첫 번째 작업이 끝난 후 두 번째 작업 실행
        CompletableFuture<Map<LocalDate, List<AiExerciseRecordDTO>>> recentExercisesRecordFuture =
                todayExerciseFatigueFuture.thenComposeAsync(fatigueResult ->
                        CompletableFuture.supplyAsync(() ->
                                exerciseRecordService.getRecentExercisesGroupedByDate(request.getMemberId()), asyncExecutor
                        ).exceptionally(ex -> {
                            System.err.println("⚠️ 운동 기록 조회 실패: " + ex.getMessage());
                            return Collections.emptyMap();
                        })
                );


        // ✅ 3️⃣ 모든 비동기 작업이 끝나면 AI 추천 요청 전송
        return CompletableFuture.allOf(todayExerciseFatigueFuture, recentExercisesRecordFuture)
                .thenCompose(voided -> {
                    Map<String, List<MuscleFatigueDTO>> todayExerciseFatigue = todayExerciseFatigueFuture.join();
                    Map<LocalDate, List<AiExerciseRecordDTO>> recentExercisesRecord = recentExercisesRecordFuture.join();

                    // DTO 생성 (없는 데이터는 기본값 처리)
                    AiRecommendationRequestDTO aiRequestDto = new AiRecommendationRequestDTO(
                            defaltExerciseSetting,
                            todayExerciseFatigue,
                            recentExercisesRecord
                    );

                    // DTO를 JSON으로 변환
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    String jsonPayload;
                    try {
                        jsonPayload = objectMapper.writeValueAsString(aiRequestDto);
                    } catch (JsonProcessingException e) {
                        return CompletableFuture.completedFuture(
                                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("error", "JsonProcessingException", "message", e.getMessage()))
                        );
                    }

                    // AI 요청 실행
                    return aiRecommendationService.sendAiRequest(jsonPayload, date, request.getMemberId());
                });
    }



    @PostMapping(value = "/autoAdaptExercises", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> autoAdaptExercises(@RequestBody AutoAdaptDTO request) {
        try {

            String memberId = request.getMemberId();

            // ✅ 운동 리스트 조회
            List<Exercise> exercises = autoAdaptService.getExercises(request);

            if (exercises.isEmpty()) {

                PriorityPartsRequestDTO requestDTO = new PriorityPartsRequestDTO();

                requestDTO.setMemberId(memberId);
                requestDTO.setPriorityParts("자동");

                exerciseSettingService.updatePriorityDefaltSetting(requestDTO);

            }

            // ✅ 성공 시 운동 리스트를 JSON으로 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status", "success", "exercises", exercises));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }





}
