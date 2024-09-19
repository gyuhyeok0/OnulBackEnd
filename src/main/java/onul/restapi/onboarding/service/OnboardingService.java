package onul.restapi.onboarding.service;

import onul.restapi.member.entity.Members;
import onul.restapi.onboarding.dto.OnboardingDTO;
import onul.restapi.onboarding.entity.OnboardingEntity;
import onul.restapi.onboarding.rapository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OnboardingService {

    private final OnboardingRepository onboardingRepository;

    @Autowired
    public OnboardingService(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }

    // 온보딩이 필요한지 여부를 체크하는 메서드
    public boolean checkIfNeedsOnboarding(String memberId) {

        // 결과 출력
        System.out.println("Member ID: " + memberId);

        // 회원의 Onboarding 정보가 없는 경우 true 반환 (즉, 온보딩이 필요)
        return onboardingRepository.findByMember_MemberId(memberId).isEmpty();
    }

    // 온보딩 등록 및 업데이트 메소드
    public void registerOnboarding(OnboardingDTO onboardingDTO) {
        String memberId = onboardingDTO.getMemberId();
        Optional<OnboardingEntity> existingOnboarding = onboardingRepository.findByMember_MemberId(memberId);

        if (existingOnboarding.isPresent()) {
            // 기존 온보딩 정보가 있을 경우 업데이트
            OnboardingEntity onboardingEntity = existingOnboarding.get();
            updateOnboardingEntity(onboardingEntity, onboardingDTO);
            onboardingRepository.save(onboardingEntity);
        } else {
            // 기존 온보딩 정보가 없을 경우 새로 등록
            OnboardingEntity onboardingEntity = convertToEntity(onboardingDTO);
            onboardingRepository.save(onboardingEntity);
        }
    }

    // Members 엔티티 객체를 생성하는 메소드 추가 (memberId는 String 타입으로 변경됨)
    private Members convertMemberIdToMembers(String memberId) {
        Members member = new Members();
        member.setMemberId(memberId);  // memberId를 String 타입으로 설정
        return member;
    }

    // DTO를 Entity로 변환
    private OnboardingEntity convertToEntity(OnboardingDTO onboardingDTO) {
        // DTO의 memberId를 Members 객체로 변환
        Members member = convertMemberIdToMembers(onboardingDTO.getMemberId());

        return OnboardingEntity.builder()
                .gender(onboardingDTO.getGender())
                .height(onboardingDTO.getHeight())
                .weight(onboardingDTO.getWeight())
                .heightUnit(onboardingDTO.getHeightUnit())
                .weightUnit(onboardingDTO.getWeightUnit())
                .basicUnit(onboardingDTO.getBasicUnit())
                .benchPress1rm(onboardingDTO.getBenchPress1rm())  // 소문자 1rm 유지
                .deadlift1rm(onboardingDTO.getDeadlift1rm())      // 소문자 1rm 유지
                .squat1rm(onboardingDTO.getSquat1rm())            // 소문자 1rm 유지
                .member(member)  // Members 객체를 전달
                .build();
    }

    // 온보딩 엔티티 업데이트 메서드
    private void updateOnboardingEntity(OnboardingEntity onboardingEntity, OnboardingDTO onboardingDTO) {
        onboardingEntity.setGender(onboardingDTO.getGender());
        onboardingEntity.setHeight(onboardingDTO.getHeight());
        onboardingEntity.setWeight(onboardingDTO.getWeight());
        onboardingEntity.setHeightUnit(onboardingDTO.getHeightUnit());
        onboardingEntity.setWeightUnit(onboardingDTO.getWeightUnit());
        onboardingEntity.setBasicUnit(onboardingDTO.getBasicUnit());
        onboardingEntity.setBenchPress1rm(onboardingDTO.getBenchPress1rm());
        onboardingEntity.setDeadlift1rm(onboardingDTO.getDeadlift1rm());
        onboardingEntity.setSquat1rm(onboardingDTO.getSquat1rm());
    }
}
