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
    public ResponseEntity<StateResponse> saveIntensity(@RequestBody IntensityDTO intensityDTO) {
        try {
            System.out.println(intensityDTO.getIntensity());
            intensityService.saveIntensity(intensityDTO.getMemberId(), intensityDTO.getIntensity());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("ERROR"));
        }
    }


}
