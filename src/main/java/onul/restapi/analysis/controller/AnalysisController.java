package onul.restapi.analysis.controller;

import jakarta.servlet.http.HttpServletRequest;
import onul.restapi.analysis.dto.ExerciseVolumeDataResponse;
import onul.restapi.analysis.dto.ExerciseVolumeResponse;
import onul.restapi.analysis.dto.WeightAndDietStatisticsDTO;
import onul.restapi.analysis.entity.WeightAndDietStatistics;
import onul.restapi.analysis.service.AnalysisService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    // AnalysisService 주입 (생성자 기반 DI)
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAnalysis(@RequestParam("memberId") String memberId) {

        try {
            // 일별 운동량 통계 업데이트
            try {
                analysisService.updateVolumeStatistics(memberId);
            } catch (Exception e) {
            }

            // 몸무게 및 식단 통계
            try {
                analysisService.updateWeightAndDietStatistics(memberId);
            } catch (Exception e) {
            }

            // 근육피로도
            try {
                analysisService.updateMuscleFatigue(memberId);
            } catch (Exception e) {
            }

            return ResponseEntity.ok("Analysis data updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
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

        System.out.println(response);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response); // Return the statistics data
    }

}
