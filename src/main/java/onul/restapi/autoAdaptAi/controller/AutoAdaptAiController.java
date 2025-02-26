package onul.restapi.autoAdaptAi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import onul.restapi.analysis.dto.ExerciseVolumeResponse;
import onul.restapi.analysis.dto.MuscleFatigueDTO;
import onul.restapi.analysis.service.AnalysisService;
import onul.restapi.autoAdaptAi.dto.*;
import onul.restapi.autoAdaptAi.entity.AutoAdaptEntity;
import onul.restapi.autoAdaptAi.service.AutoAdaptService;
import onul.restapi.autoAdaptAi.service.ExerciseSettingService;
import onul.restapi.exercise.dto.ExerciseRecordDTO;
import onul.restapi.exercise.entity.AiExerciseRecordDTO;
import onul.restapi.exercise.entity.Exercise;
import onul.restapi.exercise.service.ExerciseRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    private final AutoAdaptService autoAdaptService;

    public AutoAdaptAiController(ExerciseSettingService exerciseSettingService, AnalysisService analysisService, ExerciseRecordService exerciseRecordService, AutoAdaptService autoAdaptService) {
        this.exerciseSettingService = exerciseSettingService;
        this.analysisService = analysisService;
        this.exerciseRecordService = exerciseRecordService;
        this.autoAdaptService = autoAdaptService;
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
    public ResponseEntity<?> requestAiRecommendation(@RequestBody AutoAdaptRequestDTO request, @RequestParam("date") LocalDate date) throws JsonProcessingException {

        // true: 생성할때 "자동" 으로만 넣어야해
        System.out.println(request.isInitialization());

        // true : 날짜 확인후 날짜가 있으면 그대로 종류, 없으면 새로 생성
        // false : 날짜 확인 안해도 됨
        System.out.println(request.isCheckDate());


        // 조건문 밖에서 선언
        AutoAdaptSettingDTO defaltExerciseSetting;


        if(request.isCheckDate()){


            // ✅ 오늘 날짜에 데이터가 있는지 확인
            boolean exists = autoAdaptService.existsAutoAdaptForToday(request.getMemberId(),date);

            if (exists) {
                System.out.println("✅ 오늘 날짜에 데이터가 이미 존재하므로 종료");
                return null;  // 데이터가 있으면 종료
            }

            // 없을때 자동으로
            defaltExerciseSetting = exerciseSettingService.selectAutoAdaptSetting(request.getMemberId());
            // "자동" 하나만 포함된 새 리스트를 생성
            List<String> newPriorityParts = Collections.singletonList("자동");
            // 기존 값을 무시하고 새 리스트로 설정
            defaltExerciseSetting.setPriorityParts(newPriorityParts);

        } else {

            System.out.println("setting 직접 변경시 실행");
            defaltExerciseSetting = exerciseSettingService.selectAutoAdaptSetting(request.getMemberId());
        }

        System.out.println(defaltExerciseSetting);

        //2 오늘 운동 피로도
        Map<String, List<MuscleFatigueDTO>> todayExerciseFatigue = analysisService.getMuscleFatigueByMemberAndToday(request.getMemberId(), date);

        // 날짜별 운동 기록 가져오기
        Map<LocalDate, List<AiExerciseRecordDTO>> recentExercisesRecord = exerciseRecordService.getRecentExercisesGroupedByDate(request.getMemberId());

        // ✅ DTO 생성 (없는 데이터는 기본값 처리)
        AiRecommendationRequestDTO aiRequestDto = new AiRecommendationRequestDTO(
                defaltExerciseSetting,
//                recentVolumeByMuscleGroup, // 없으면 null
                (todayExerciseFatigue != null) ? todayExerciseFatigue : Collections.emptyMap(), // 없으면 빈 맵
                (recentExercisesRecord != null) ? recentExercisesRecord : Collections.emptyMap() // 없으면 빈 맵
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // DTO를 JSON으로 변환
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String jsonPayload = objectMapper.writeValueAsString(aiRequestDto);
        System.out.println("📌 변환된 JSON: " + jsonPayload);

        try {
//            HttpClient client = HttpClient.newHttpClient();

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)  // 🚀 HTTP/1.1로 강제 설정
                    .build();


            // ✅ Python 서버로 HTTP POST 요청
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://127.0.0.1:8000/aiRequest"))  // Python 서버 주소
                    .header("Content-Type", "application/json")  // JSON 데이터로 전송
                    .header("Accept", "application/json")  // JSON 응답 기대
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))  // JSON 데이터 전송
                    .build();

            // ✅ 응답 받기
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // ✅ 응답 로그 출력
            System.out.println("📌 [Java] 응답 상태 코드: " + response.statusCode());
            System.out.println("📌 [Java] 응답 본문: " + response.body());

            // ✅ Python 서버 응답 출력
            System.out.println( response.body());
            System.out.println(date);
            System.out.println(request.getMemberId());

            String memberId = request.getMemberId();

            String jsonResponse = response.body();  // responseBody를 jsonResponse로 저장

            // ✅ Jackson ObjectMapper 생성
            ObjectMapper objectMapperPython = new ObjectMapper();

            // ✅ JSON 배열을 List<Long>로 변환
            List<Long> exerciseList = objectMapperPython.readValue(jsonResponse, new TypeReference<List<Long>>() {});

            // ✅ AutoAdaptDTO 객체 생성
            AutoAdaptDTO autoAdaptDTO = new AutoAdaptDTO(exerciseList, date, memberId);

            System.out.println("최종"+autoAdaptDTO);
            AutoAdaptEntity savedEntity = autoAdaptService.saveOrUpdateAutoAdapt(autoAdaptDTO);


            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Python 서버 요청 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @PostMapping(value = "/autoAdaptExercises", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> autoAdaptExercises(@RequestBody AutoAdaptDTO request) {
        try {
            System.out.println("안녕시발아");
            System.out.println(request);

            // ✅ 운동 리스트 조회
            List<Exercise> exercises = autoAdaptService.getExercises(request);
            System.out.println(exercises);

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
