package onul.restapi.analysis.controller;

import onul.restapi.analysis.service.AnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
