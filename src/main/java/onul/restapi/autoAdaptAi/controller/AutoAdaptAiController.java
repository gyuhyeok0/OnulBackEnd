package onul.restapi.autoAdaptAi.controller;

import onul.restapi.analysis.dto.MuscleFatigueDTO;
import onul.restapi.autoAdaptAi.dto.AutoAdaptSettingDTO;
import onul.restapi.autoAdaptAi.dto.AutoAdaptSettingRequstDTO;
import onul.restapi.autoAdaptAi.service.ExerciseSettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/autoAdapt")
public class AutoAdaptAiController {

    private final ExerciseSettingService exerciseSettingService;

    public AutoAdaptAiController(ExerciseSettingService exerciseSettingService) {
        this.exerciseSettingService = exerciseSettingService;
    }

    @GetMapping(value = "/getAutoAdaptSetting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAutoAdaptSetting(@RequestParam("memberId") String memberId) {
        AutoAdaptSettingDTO response = exerciseSettingService.selectAutoAdaptSetting(memberId);

        System.out.println(response);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(value = "/updateAutoAdaptSetting", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> updateAutoAdaptSetting(@RequestBody AutoAdaptSettingRequstDTO request) {

        System.out.println(request);
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


}
