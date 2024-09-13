package onul.restapi.onboarding.service;

import onul.restapi.onboarding.rapository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardingService {


    private final OnboardingRepository onboardingRepository;

    @Autowired
    public OnboardingService(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }

    // 온보딩이 필요한지 여부를 체크하는 메서드
    public boolean checkIfNeedsOnboarding(String memberId) {

        // 회원의 Onboarding 정보가 없는 경우 true 반환 (즉, 온보딩이 필요)
        return onboardingRepository.findByMember_MemberId(memberId).isEmpty();
    }
}
