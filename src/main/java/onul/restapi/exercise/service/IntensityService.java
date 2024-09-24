package onul.restapi.exercise.service;

import jakarta.transaction.Transactional;
import onul.restapi.exercise.entity.IntensityEntity;
import onul.restapi.exercise.repository.IntensityRepository;
import onul.restapi.member.entity.Members;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class IntensityService {

    private final IntensityRepository intensityRepository;
    private final MemberRepository memberRepository;

    public IntensityService(IntensityRepository intensityRepository, MemberRepository memberRepository) {
        this.intensityRepository = intensityRepository;
        this.memberRepository = memberRepository;
    }

    public void saveIntensity(String memberId, String intensity) {
        // 오늘 날짜를 가져옵니다.
        LocalDate today = LocalDate.now();

        // LocalDate를 Date로 변환
        Date todayStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // memberId로 Members 엔티티 조회
        Members member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 해당 사용자와 오늘 날짜의 기록을 찾습니다.
        IntensityEntity existingIntensity = intensityRepository.findByMemberAndCreatedAt(member, todayStart);

        if (existingIntensity != null) {
            // 이미 존재하는 경우 업데이트
            IntensityEntity updatedIntensityEntity = IntensityEntity.builder()
                    .id(existingIntensity.getId()) // ID 유지
                    .member(existingIntensity.getMember()) // 사용자 유지
                    .intensity(intensity) // 새로운 강도 설정
                    .createdAt(todayStart) // createdAt 추가
                    .build();
            intensityRepository.save(updatedIntensityEntity);
        } else {
            // 존재하지 않는 경우 새로 생성
            IntensityEntity intensityEntity = IntensityEntity.builder()
                    .member(member)
                    .intensity(intensity)
                    .createdAt(todayStart) // createdAt 추가
                    .build();
            intensityRepository.save(intensityEntity);
        }
    }
}
