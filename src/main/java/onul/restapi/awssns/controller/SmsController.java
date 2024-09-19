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

        // SmsService에서 요청을 처리하고 상태를 반환
        SmsResponse response = smsService.sendSms(smsRequest.getPhoneNumber(), false);

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


    //인증 코드를 검증
    @PostMapping("/verify")
    public String verifyCode(@RequestBody VerificationRequestDTO verificationRequest) {
        String phoneNumber = verificationRequest.getPhoneNumber();
        String code = verificationRequest.getCode();

        System.out.println("실행중" + phoneNumber);

        // 전화번호 해시화
        String hashedPhoneNumber = smsService.hashPhoneNumber(phoneNumber);

        System.out.println("전화번호 해쉬번호" + hashedPhoneNumber);

        // 해시화된 전화번호로 코드 엔티티 조회
        Optional<CodeEntity> codeEntityOptional = Optional.ofNullable(codeRepository.findByPhoneNumber(hashedPhoneNumber));

        System.out.println(codeEntityOptional.isPresent());
        System.out.println(code);

        // 전화번호가 없으면 반환 (true)
        if (codeEntityOptional.isEmpty()) {
            System.out.println("전화번호가 존재하지 않습니다");
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


    @PostMapping("/check-id")
    public ResponseEntity<UserResponse> checkUserId(@RequestBody UserIdRequest request) {
        String userId = request.getUserId(); // 아이디 가져오기
        System.out.println(request);

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

        System.out.println(userId);
        System.out.println(inputPhoneNumber);

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
        System.out.println("Hashed phone number from database: " + storedHashedPhoneNumber);

        if (!hashedPhoneNumber.equals(storedHashedPhoneNumber)) {
            System.out.println("Phone number does not match for userId: " + userId);
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
