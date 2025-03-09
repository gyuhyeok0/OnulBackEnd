package onul.restapi.exercise.controller;

import onul.restapi.exercise.dto.IntensityDTO;
import onul.restapi.exercise.service.IntensityService;
import onul.restapi.member.controller.StateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/intensity")
public class IntensityController {

    private final IntensityService intensityService;

    public IntensityController(IntensityService intensityService) {
        this.intensityService = intensityService;
    }


    @PostMapping("/intensity")
    public ResponseEntity<?> saveIntensity(@RequestBody IntensityDTO intensityDTO) {
        try {
            // DTO에서 값 확인

            // 서비스 호출 및 저장된 데이터 반환
            IntensityDTO savedIntensity = intensityService.saveIntensity(intensityDTO.getMemberId(), intensityDTO.getIntensity());

            // 저장된 데이터를 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedIntensity);

        } catch (IllegalArgumentException e) {
            // 잘못된 입력 처리
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("INVALID_MEMBER_ID"));
        } catch (Exception e) {
            // 기타 에러 처리
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("ERROR"));
        }
    }



}
