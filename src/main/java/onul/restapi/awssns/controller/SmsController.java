package onul.restapi.awssns.controller;

import onul.restapi.awssns.dto.VerificationRequestDTO;
import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.awssns.service.SmsService;
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
    public String sendSms(@RequestBody VerificationRequestDTO smsRequest) {
        return smsService.sendSms(smsRequest.getPhoneNumber());
    }


    @PostMapping("/verify")
    public String verifyCode(@RequestBody VerificationRequestDTO verificationRequest) {
        String phoneNumber = verificationRequest.getPhoneNumber();
        String code = verificationRequest.getCode();

        System.out.println(code);
        // 데이터베이스에서 전화번호로 코드 엔티티 조회
        Optional<CodeEntity> codeEntityOptional = Optional.ofNullable(codeRepository.findByPhoneNumber(phoneNumber));

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
