package onul.restapi.onboarding.controller;

import onul.restapi.onboarding.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
