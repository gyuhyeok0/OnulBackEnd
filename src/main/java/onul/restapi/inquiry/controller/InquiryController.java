package onul.restapi.inquiry.controller;

import onul.restapi.inquiry.dto.InquiryDTO;
import onul.restapi.inquiry.service.InquiryService;
import onul.restapi.member.controller.StateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/submit")
    public ResponseEntity<StateResponse> submitInquiry(@RequestBody InquiryDTO inquiryDTO) {

        // 1. 기본 유효성 검사
        if (inquiryDTO.getEmail() == null || inquiryDTO.getTitle() == null || inquiryDTO.getContent() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)  // 응답 타입 명시
                    .body(new StateResponse("INVALID_INPUT"));  // 상태 정보만 반환
        }

        // 2. 이메일 형식 유효성 검사
        if (!isValidEmail(inquiryDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new StateResponse("INVALID_EMAIL"));
        }

        // 3. 문의 데이터 저장
        inquiryService.submitInquiry(inquiryDTO);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new StateResponse("SUCCESS"));
    }

    // 이메일 형식 유효성 검사 메서드
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

}
