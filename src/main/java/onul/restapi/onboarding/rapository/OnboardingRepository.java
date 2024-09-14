package onul.restapi.onboarding.rapository;


import onul.restapi.onboarding.entity.OnboardingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnboardingRepository extends JpaRepository<OnboardingEntity, Long> {

    // 특정 회원(memberId)의 온보딩 정보를 조회하는 메서드
    Optional<OnboardingEntity> findByMember_MemberId(String memberId);
}
