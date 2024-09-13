package onul.restapi.onboarding.controller;

import onul.restapi.onboarding.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkOnboardingStatus(@RequestParam String memberId) {
        boolean needsOnboarding = onboardingService.checkIfNeedsOnboarding(memberId);

        // Boolean 값만 바로 반환
        return ResponseEntity.ok(needsOnboarding);
    }

}
