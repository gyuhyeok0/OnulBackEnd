package onul.restapi.awssns.controller;

import onul.restapi.awssns.dto.VerificationRequestDTO;
import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.awssns.service.SmsResponse;
import onul.restapi.awssns.service.SmsService;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;
    private final CodeRepository codeRepository;
    private final MemberRepository memberRepository;

    public SmsController(SmsService smsService, CodeRepository codeRepository, MemberRepository memberRepository) {
        this.smsService = smsService;
        this.codeRepository = codeRepository;
        this.memberRepository = memberRepository;
    }


    // sms 인증번호 요청
    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody VerificationRequestDTO smsRequest) {
        String phoneNumber = smsRequest.getPhoneNumber();

        // SmsService에서 요청을 처리하고 상태를 반환
        SmsResponse response = smsService.sendSms(phoneNumber, false);

        // 실패 케이스만 로그
        if ("LIMIT_EXCEEDED".equals(response.getStatus())) {
            log.warn("상태: LIMIT_EXCEEDED (시간 제한 초과)");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        if ("DAILY_LIMIT_EXCEEDED".equals(response.getStatus())) {
            log.warn("상태: DAILY_LIMIT_EXCEEDED (일일 제한 초과)");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        // 성공 응답 (로그 없음)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    //인증 코드를 검증
    @PostMapping("/verify")
    public String verifyCode(@RequestBody VerificationRequestDTO verificationRequest) {
        String phoneNumber = verificationRequest.getPhoneNumber();
        String code = verificationRequest.getCode().trim(); // 공백 제거


        // 전화번호 해시화
        String hashedPhoneNumber = smsService.hashPhoneNumber(phoneNumber);


        // 해시화된 전화번호로 코드 엔티티 조회
        Optional<CodeEntity> codeEntityOptional = Optional.ofNullable(codeRepository.findByPhoneNumber(hashedPhoneNumber));


        // 전화번호가 없으면 반환 (true)
        if (codeEntityOptional.isEmpty()) {
            return "Invalid phone number";
        }

        CodeEntity codeEntity = codeEntityOptional.get();

        // 코드와 만료 시간 검증
        if (code.equals(codeEntity.getCode()) && !isExpired(codeEntity.getExpiryTime())) {
            return "Verification successful";
        } else {
            if (!code.equals(codeEntity.getCode())) {
                log.warn("인증 코드 불일치 - 입력: {}, 저장: {}", code, codeEntity.getCode());
            }
            return "Invalid or expired code";
        }
    }


    // 코드 만료 여부를 확인하는 메서드
    private boolean isExpired(Long expiryTime) {
        return System.currentTimeMillis() > expiryTime;
    }


    @PostMapping("/check-id")
    public ResponseEntity<UserResponse> checkUserId(@RequestBody UserIdRequest request) {
        String userId = request.getUserId(); // 아이디 가져오기

        // 아이디로 회원 조회
        Members member = memberRepository.findByMemberId(userId);

        UserResponse response = new UserResponse();
        response.setExists(member != null); // 존재 여부 설정

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // 응답 타입 명시
                .body(response); // JSON 응답 반환
    }


    @PostMapping("/verificationAndSend")
    public ResponseEntity<SmsResponse> verificationAndSend(@RequestBody VerificationRequestDTO smsRequest) {
        String userId = smsRequest.getMemberId();
        String inputPhoneNumber = smsRequest.getPhoneNumber();


        // 아이디로 회원 조회
        Members member = memberRepository.findByMemberId(userId);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SmsResponse("INVALID_USER_ID")); // 상태 코드로 전송
        }

        // 전화번호 해시화
        String hashedPhoneNumber = smsService.hashPhoneNumber(inputPhoneNumber);

        // 저장된 해시 전화번호 가져오기
        String storedHashedPhoneNumber = member.getMemberPhoneNumber();

        if (!hashedPhoneNumber.equals(storedHashedPhoneNumber)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN) // 또는 HttpStatus.NOT_FOUND
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SmsResponse("PHONE_NOT_REGISTERED")); // 상태 코드로 전송
        }


        // SmsService에서 요청을 처리하고 상태를 반환
        SmsResponse response = smsService.sendSms(inputPhoneNumber, true);

        // 상태에 따라 적절한 HTTP 응답 반환
        if ("LIMIT_EXCEEDED".equals(response.getStatus()) || "DAILY_LIMIT_EXCEEDED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SmsResponse(response.getStatus())); // 상태 코드로 전송
        }

        // 성공적인 요청 처리
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SmsResponse("SUCCESS")); // 상태 코드로 전송
    }










}
