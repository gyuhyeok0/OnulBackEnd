package onul.restapi.member.controller;

import onul.restapi.common.ErrorResponse;
import onul.restapi.common.SuccessResponse;
import onul.restapi.member.dto.MemberDTO;
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
    private final MemberDTO memberDTO;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
        this.memberDTO = new MemberDTO();
    }


    @PostMapping ("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO request) {

        // 유효성 검사 정규식 (클라이언트에서 사용한 것과 동일)
        String memberIdRegex = "^(?=[a-zA-Z0-9]{6,})(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]*$"; // 영문자와 숫자 모두 포함, 6자리 이상
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9]).{6,20}$"; // 영문자와 숫자 필수, 6-20자리

        // 요청 데이터 유효성 검사
        if (request.getMemberId() == null || request.getMemberPassword() == null ||
                request.getMemberCountryCode() == null || request.getMemberPhoneNumber() == null ||
                !request.isAgreed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(false, "모든 필드를 입력해 주세요."));
        }

        // 서버에서 아이디 유효성 검사
        if (!request.getMemberId().matches(memberIdRegex)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(false, "아이디는 영문자와 숫자가 포함된 6자리 이상이어야 합니다."));
        }

        // 서버에서 비밀번호 유효성 검사
        if (!request.getMemberPassword().matches(passwordRegex)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SignupResponse(false, "비밀번호는 영문자와 숫자가 포함된 6~20자리여야 합니다."));
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
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 타입 명시
                    .body(new SignupResponse(false, "회원가입 중 오류가 발생했습니다."));
        }
    }



    // 아이디 중복확인 (상태만 반환)
    @PostMapping("/checkDuplicate")
    public ResponseEntity<?> checkMemberIdDuplicate(@RequestBody MemberDTO memberDTO) {
        // 유효성 검사 정규식 (클라이언트와 동일한 정규식 사용)
        String memberIdRegex = "^(?=[a-zA-Z0-9]{6,})(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]*$"; // 영문자와 숫자 모두 포함, 6자리 이상

        // 아이디 유효성 검사
        if (memberDTO.getMemberId() == null || !memberDTO.getMemberId().matches(memberIdRegex)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 타입 명시
                    .body(new StateResponse("INVALID_ID")); // 유효하지 않은 아이디 상태
        }

        // 서비스에서 아이디 중복 여부 확인
        boolean isDuplicate = signupService.isMemberIdDuplicate(memberDTO.getMemberId());

        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 타입 명시
                    .body(new StateResponse("DUPLICATE")); // 중복 아이디 상태
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 타입 명시
                    .body(new StateResponse("AVAILABLE")); // 사용 가능한 아이디 상태
        }
    }

}
