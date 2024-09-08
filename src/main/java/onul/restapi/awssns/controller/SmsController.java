package onul.restapi.awssns.controller;

import onul.restapi.awssns.dto.VerificationRequestDTO;
import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.awssns.service.SmsResponse;
import onul.restapi.awssns.service.SmsService;
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


    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody VerificationRequestDTO smsRequest) {
        System.out.println(smsRequest.getPhoneNumber());

        // SmsResponse 객체를 반환 (상태만 포함)
        SmsResponse response = smsService.sendSms(smsRequest.getPhoneNumber());

        System.out.println("응답: " + response);

        // JSON 형식으로 응답을 반환 (상태만 포함)
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
