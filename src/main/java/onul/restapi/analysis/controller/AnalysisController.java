package onul.restapi.analysis.controller;

import jakarta.servlet.http.HttpServletRequest;
import onul.restapi.analysis.dto.ExerciseVolumeDataResponse;
import onul.restapi.analysis.dto.ExerciseVolumeResponse;
import onul.restapi.analysis.dto.MuscleFatigueDTO;
import onul.restapi.analysis.dto.WeightAndDietStatisticsDTO;
import onul.restapi.analysis.entity.MuscleFatigue;
import onul.restapi.analysis.entity.WeightAndDietStatistics;
import onul.restapi.analysis.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    @Qualifier("analysis")
    private Executor asyncExecutor;

    private final AnalysisService analysisService;

    // AnalysisService 주입 (생성자 기반 DI)
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateAnalysis(
            @RequestParam("memberId") String memberId,
            @RequestParam("date") LocalDate date) {

        try {
            CompletableFuture.runAsync(() -> analysisService.updateVolumeStatistics(memberId, date), asyncExecutor)
                    .thenRunAsync(() -> analysisService.updateWeightAndDietStatistics(memberId, date), asyncExecutor)
                    .thenRunAsync(() -> analysisService.updateMuscleFatigue(memberId, date), asyncExecutor)
                    .exceptionally(ex -> {  // ✅ 예외 발생 시 로깅 및 처리
                        System.err.println("Error occurred in analysis tasks: " + ex.getMessage());
                        return null;  // 예외가 발생해도 흐름이 깨지지 않도록 처리
                    })
                    .join();

            return ResponseEntity.ok("All analysis tasks completed successfully.");
        } catch (CompletionException e) {
            // ✅ 내부 예외를 꺼내서 처리 (원래 예외를 추출)
            Throwable cause = e.getCause();
            return ResponseEntity.status(500).body("An error occurred: " + cause.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @GetMapping(value = "/findExerciseVolume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExerciseVolumeResponse> findExerciseVolume(@RequestParam("memberId") String memberId) {
        LocalDate endDate = LocalDate.now().minusDays(1); // 어제 (오늘 제외)
        LocalDate startDate = endDate.minusDays(6); // 어제로부터 6일 전 (총 7일간 조회)

        ExerciseVolumeResponse response = analysisService.getExerciseVolume(memberId, startDate, endDate);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // 주간, 월간 운동 볼륨
    @GetMapping(value = "/WeeklyAndMonthlyExerciseVolume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> WeeklyAndMonthlyExerciseVolume(@RequestParam("memberId") String memberId) {

        ExerciseVolumeDataResponse response = analysisService.getWeeklyAndMonthlyVolume(memberId);  // 서비스에서 데이터 가져오기

        return ResponseEntity.ok()  // 성공적인 응답 반환
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);  // 응답 본문에 데이터 추가
    }


    @GetMapping(value = "/MonthlyWeightAndDiet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMonthlyWeightAndDiet(@RequestParam("memberId") String memberId) {
        List<WeightAndDietStatisticsDTO> response = analysisService.getMonthlyStatistics(memberId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response); // Return the statistics data
    }


    @GetMapping(value = "/getMuscleFaigue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMuscleFatigue(@RequestParam("memberId") String memberId, @RequestParam("date") LocalDate date) {

        // 서비스에서 오늘 날짜 기준의 데이터를 조회하고 그룹화된 결과를 받아옴
        Map<String, List<MuscleFatigueDTO>> response = analysisService.getMuscleFatigueByMemberAndToday(memberId, date);

        // 응답을 JSON 형태로 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response); // 근육 그룹별 피로도 리스트
    }

}
