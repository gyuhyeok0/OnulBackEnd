package onul.restapi.awssns.controller;

import onul.restapi.awssns.dto.VerificationRequestDTO;
import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.awssns.service.SmsResponse;
import onul.restapi.awssns.service.SmsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;
    private final CodeRepository codeRepository;

    public SmsController(SmsService smsService, CodeRepository codeRepository) {
        this.smsService = smsService;
        this.codeRepository = codeRepository;
    }


    // sms 인증번호 요청
    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody VerificationRequestDTO smsRequest) {
        System.out.println(smsRequest.getPhoneNumber());

        // SmsService에서 요청을 처리하고 상태를 반환
        SmsResponse response = smsService.sendSms(smsRequest.getPhoneNumber());

        System.out.println("응답: " + response);

        // 상태에 따라 적절한 HTTP 응답 반환
        if ("LIMIT_EXCEEDED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON) // 응답을 JSON으로 반환
                    .body(response);
        }

        // 일일 요청 횟수 초과 시 처리
        if ("DAILY_LIMIT_EXCEEDED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON) // 응답을 JSON으로 반환
                    .body(response);
        }

        // 성공적인 요청 처리
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // 응답 타입 명시
                .body(response);  // 상태만 반환
    }



    @PostMapping("/verify")
    public String verifyCode(@RequestBody VerificationRequestDTO verificationRequest) {
        String phoneNumber = verificationRequest.getPhoneNumber();
        String code = verificationRequest.getCode();

        // 전화번호 해시화
        String hashedPhoneNumber = smsService.hashPhoneNumber(phoneNumber);

        // 해시화된 전화번호로 코드 엔티티 조회
        Optional<CodeEntity> codeEntityOptional = Optional.ofNullable(codeRepository.findByPhoneNumber(hashedPhoneNumber));

        if (codeEntityOptional.isEmpty()) {
            return "Invalid phone number";
        }

        CodeEntity codeEntity = codeEntityOptional.get();

        // 코드와 만료 시간 검증
        if (code.equals(codeEntity.getCode()) && !isExpired(codeEntity.getExpiryTime())) {
            return "Verification successful";
        } else {
            return "Invalid or expired code";
        }
    }

    // 코드 만료 여부를 확인하는 메서드
    private boolean isExpired(Long expiryTime) {
        return System.currentTimeMillis() > expiryTime;
    }

}
