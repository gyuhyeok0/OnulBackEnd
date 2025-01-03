package onul.restapi.management.controller;

import jakarta.validation.Valid;
import onul.restapi.management.dto.BodyDataDto;
import onul.restapi.management.dto.BodyDataRequest;
import onul.restapi.management.service.BodyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management")
public class BodyController {

    private final BodyService bodyService;

    public BodyController(BodyService bodyService) {
        this.bodyService = bodyService;
    }

    @PostMapping("/saveBodyData")
    public ResponseEntity<?> saveBodyData(@RequestBody BodyDataRequest request) {
        try {
            // 데이터 확인
            System.out.println("Received memberId: " + request.getMemberId());
            System.out.println("Received bodyData: " + request.getBodyData());

            // 서비스 호출
            BodyDataDto result = bodyService.saveBodyData(request.getMemberId(), request.getBodyData());

            System.out.println("저장된 데이터: " + result);

            // JSON 형태로 응답 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving body data: " + e.getMessage());
        }
    }



}
