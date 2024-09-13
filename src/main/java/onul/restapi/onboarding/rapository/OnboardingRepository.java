package onul.restapi.onboarding.rapository;


import onul.restapi.onboarding.entity.OnboardingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnboardingRepository extends JpaRepository<OnboardingEntity, Long> {
    // 회원의 memberId를 기준으로 온보딩 정보를 조회하는 메서드
    Optional<OnboardingEntity> findByMember_MemberId(String memberId);  // Members 엔티티의 memberId 필드를 경로로 참조
}
