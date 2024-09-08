package onul.restapi.member.controller;

import onul.restapi.common.ErrorResponse;
import onul.restapi.common.SuccessResponse;
import onul.restapi.member.dto.SignupRequestDTO;
import onul.restapi.member.service.SignupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping ("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO request) {

        System.out.println("컨트롤러 실행되었니?");
        System.out.println(request.getMemberPhoneNumber());

        // 요청 데이터 유효성 검사
        if (request.getMemberId() == null || request.getMemberPassword() == null ||
                request.getMemberCountryCode() == null || request.getMemberPhoneNumber() == null ||
                !request.isAgreed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(false, "모든 필드를 입력해 주세요."));
        }

        // 회원가입 서비스 호출 (핸드폰 번호 포함)
        boolean isSignupSuccess = signupService.signup(
                request.getMemberId(),
                request.getMemberPassword(),
                request.getMemberCountryCode(),
                request.getMemberPhoneNumber()
        );

        if (isSignupSuccess) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 타입 명시
                    .body(new SignupResponse(true, "회원가입이 완료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SignupResponse(false, "회원가입 중 오류가 발생했습니다."));
        }
    }
}
