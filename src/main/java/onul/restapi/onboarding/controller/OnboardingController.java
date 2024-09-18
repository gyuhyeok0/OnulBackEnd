package onul.restapi.onboarding.controller;

import onul.restapi.onboarding.dto.OnboardingDTO;
import onul.restapi.onboarding.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @Autowired
    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkOnboardingStatus(@RequestParam String memberId) {
        boolean needsOnboarding = onboardingService.checkIfNeedsOnboarding(memberId);

        System.out.println(needsOnboarding);
        return ResponseEntity.ok(needsOnboarding);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerOnboarding(@RequestBody OnboardingDTO onboardingRequest) {
        System.out.println("되니?");
        System.out.println(onboardingRequest);

        // 유효성 검사
        if (onboardingRequest.getMemberId() == null || onboardingRequest.getMemberId().isEmpty()) {
            return ResponseEntity.badRequest().body("회원 ID가 필요합니다.");
        }
        if (onboardingRequest.getGender() == null || onboardingRequest.getGender().isEmpty()) {
            return ResponseEntity.badRequest().body("성별이 필요합니다.");
        }

        // 키와 몸무게 유효성 검사
        if (onboardingRequest.getHeight() < 0 || onboardingRequest.getHeight() > 304800 ||
                Math.round(onboardingRequest.getHeight() * 100) / 100.0 != onboardingRequest.getHeight()) {
            return ResponseEntity.badRequest().body("유효한 키를 입력해야 하며, 0 이상 10000 이하의 소수점 2자리까지 가능합니다.");
        }
        if (onboardingRequest.getWeight() < 0 || onboardingRequest.getWeight() > 10000 ||
                Math.round(onboardingRequest.getWeight() * 100) / 100.0 != onboardingRequest.getWeight()) {
            return ResponseEntity.badRequest().body("유효한 몸무게를 입력해야 하며, 0 이상 10000 이하의 소수점 2자리까지 가능합니다.");
        }

        // 1RM 유효성 검사
        if (onboardingRequest.getBenchPress1rm() < -1 || onboardingRequest.getBenchPress1rm() > 1000) {
            return ResponseEntity.badRequest().body("벤치프레스 1RM은 0 이상 1000 이하이어야 합니다.");
        }
        if (onboardingRequest.getDeadlift1rm() < -1 || onboardingRequest.getDeadlift1rm() > 1000) {
            return ResponseEntity.badRequest().body("데드리프트 1RM은 0 이상 1000 이하이어야 합니다.");
        }
        if (onboardingRequest.getSquat1rm() < -1 || onboardingRequest.getSquat1rm() > 1000) {
            return ResponseEntity.badRequest().body("스쿼트 1RM은 0 이상 1000 이하이어야 합니다.");
        }

        try {
            // 요청으로 받은 온보딩 데이터를 처리
            onboardingService.registerOnboarding(onboardingRequest);
            return ResponseEntity.ok("온보딩 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("온보딩 등록 중 오류 발생: " + e.getMessage());
        }
    }


}
